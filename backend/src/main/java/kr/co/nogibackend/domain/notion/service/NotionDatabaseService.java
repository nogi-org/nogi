package kr.co.nogibackend.domain.notion.service;

import static kr.co.nogibackend.global.response.code.UserResponseCode.F_NOT_FOUND_USER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kr.co.nogibackend.domain.notion.command.CompletedPageMarkdownCommand;
import kr.co.nogibackend.domain.notion.command.NotionEndTILCommand;
import kr.co.nogibackend.domain.notion.helper.NotionDataInjector;
import kr.co.nogibackend.domain.notion.helper.NotionDataProvider;
import kr.co.nogibackend.domain.notion.helper.NotionMarkdownConverter;
import kr.co.nogibackend.domain.notion.helper.NotionRequestMaker;
import kr.co.nogibackend.domain.notion.port.NotionClientPort;
import kr.co.nogibackend.domain.notion.result.CompletedPageMarkdownResult;
import kr.co.nogibackend.domain.notion.result.NotionBaseResult;
import kr.co.nogibackend.domain.notion.result.NotionBlockConversionResult;
import kr.co.nogibackend.domain.notion.result.NotionBlockResult;
import kr.co.nogibackend.domain.notion.result.NotionDatabaseResult;
import kr.co.nogibackend.domain.notion.result.NotionEndTILResult;
import kr.co.nogibackend.domain.notion.result.NotionPageResult;
import kr.co.nogibackend.domain.user.entity.User;
import kr.co.nogibackend.domain.user.port.UserRepositoryPort;
import kr.co.nogibackend.global.config.context.ExecutionResultContext;
import kr.co.nogibackend.global.config.exception.GlobalException;
import kr.co.nogibackend.infra.notion.adapter.NotionClientPortFeignAdapter;
import kr.co.nogibackend.interfaces.notion.response.NotionUserPagesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotionPageService {

	private final NotionDataProvider notionDataProvider;
	private final NotionClientPort notionClientPort;
	private final UserRepositoryPort userRepositoryPort;
	private final NotionDataInjector notionDataInjector;
	private final ObjectMapper objectMapper;
	private final NotionMarkdownConverter notionMarkdownConverter;
	private final NotionClientPortFeignAdapter notionClientFeignAdapter;

	/**
	 * <h2>📝 Notion에서 작성완료된 TIL 조회 및 Markdown 변환</h2>
	 *
	 * <ul>
	 *   <li>1️⃣ 작성완료 상태 페이지 조회</li>
	 *   <li>2️⃣ 각 페이지에 커밋 일자 및 시간 추가</li>
	 *   <li>3️⃣ Notion 블록 정보 조회</li>
	 *   <li>4️⃣ 블록 데이터를 Markdown 형식으로 변환</li>
	 *   <li>5️⃣ 변환된 결과를 NotionStartTILResult 객체로 생성</li>
	 *   <li>6️⃣ 모든 결과를 리스트에 담아 반환</li>
	 * </ul>
	 */
	public List<CompletedPageMarkdownResult> convertCompletedPageToMarkdown(
			CompletedPageMarkdownCommand command
	) {
		// 작성완료 상태의 TIL 페이지 조회
		List<NotionPageResult> pages =
				notionDataProvider
						.getCompletedPages(command.getNotionBotToken(), command.getNotionDatabaseId());

		List<CompletedPageMarkdownResult> results = new ArrayList<>();
		for (NotionPageResult page : pages) {
			try {
				// 페이지에 커밋 일자 및 시간 추가
				page.getProperties().createCommitDateWithCurrentTime();

				// Notion 블록 정보 조회
				NotionBaseResult<NotionBlockResult> blocks =
						notionDataProvider
								.getBlocks(command.getNotionBotToken(), page.getId());

				// 블록 마크다운 변환 전처리
				notionDataProvider.preprocessMarkdown(blocks, command.getNotionBotToken());

				// 블록 마크다운 변환 처리
				NotionBlockConversionResult markdown =
						notionMarkdownConverter.convert(blocks.getResults(), command.getGithubOwner());

				// 최종 데이터 결과 응답
				results.add(new CompletedPageMarkdownResult(command.getUserId(), page, markdown));
			} catch (Exception error) {
				// todo: 실패한 경우 page가 result에 안들어감. 아마도 알림에서 처리 못 할텐데...
				ExecutionResultContext
						.addNotionPageErrorResult(error.getMessage(), command.getUserId());
			}
		}

		// 변환된 결과 리스트 반환
		return results;
	}

	public NotionUserPagesResponse getPages(Long userId, String nextCursor) {
		User user = this.findUser(userId);
		NotionBaseResult<NotionPageResult> pages =
				notionClientPort.getPagesFromDatabase(
						user.getNotionAccessToken()
						, user.getNotionDatabaseId()
						, NotionRequestMaker.createPageAllFilter(nextCursor)
				);
		return NotionUserPagesResponse.of(pages);
	}

	// todo: objectMapper 예외 처리 다시 하기
	public String getPage(Long userId, String pageId) {
		User user = this.findUser(userId);
		NotionBaseResult<NotionBlockResult> blocks =
				notionDataProvider
						.getBlocks(user.getNotionAccessToken(), pageId);
		notionDataProvider.preprocessMarkdown(blocks, user.getNotionAccessToken());

		try {
			return objectMapper.writeValueAsString(blocks);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	// todo: objectMapper 예외 처리 다시 하기
	public String getDatabase(Long userId) {
		User user = this.findUser(userId);
		NotionDatabaseResult database =
				notionClientPort.getDatabase(user.getNotionAccessToken(), user.getNotionDatabaseId());
		try {
			return objectMapper.writeValueAsString(database);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	// Github 에 commit 된 결과를 notion 상태값 변경
	public List<NotionEndTILResult> updateStatusByResult(List<NotionEndTILCommand> commands) {
		return
				commands
						.stream()
						.map(this::updateStatusByResult)
						.flatMap(Optional::stream)
						.toList();
	}

	/**
	 * <h2>✅ Notion TIL 상태 업데이트</h2>
	 *
	 * <ul>
	 *   <li>1️⃣ TIL 커밋 성공/실패 여부를 기반으로 상태 업데이트</li>
	 *   <li>2️⃣ Notion 페이지 ID, 사용자 ID를 이용해 상태 변경</li>
	 *   <li>3️⃣ 성공한 경우 NotionEndTILResult 객체 생성</li>
	 *   <li>4️⃣ 실패한 경우 Optional.empty() 반환</li>
	 * </ul>
	 */
	public Optional<NotionEndTILResult> updateStatusByResult(NotionEndTILCommand command) {
		// 1️⃣ TIL 커밋 성공/실패 여부를 기반으로 상태 업데이트
		boolean isUpdateResult =
				notionDataInjector.updateTILResultStatus(
						command.isSuccess(), command.notionBotToken(), command.notionPageId(), command.userId()
				);

		// 2️⃣ Notion 페이지 ID, 사용자 ID를 이용해 상태 변경
		return
				isUpdateResult && command.isSuccess()
						? Optional.of(
						// 3️⃣ 성공한 경우 NotionEndTILResult 객체 생성
						new NotionEndTILResult(
								command.userId(),
								command.notionPageId(),
								command.category(),
								command.title(),
								command.content()
						))
						// 4️⃣ 실패한 경우 Optional.empty() 반환
						: Optional.empty();
	}

	// todo: 리팩토링 필요!
	@Transactional
	public Map<String, List<String>> updateNotionPageId() {

		List<User> users =
				userRepositoryPort.findAllUser();

		List<User> filterUsers =
				users
						.stream()
						.filter(user -> user.hasNotionDatabaseId() && user.hasNotionAccessToken())
						.toList();

		Map<String, List<String>> result = new HashMap<>();
		List<String> success = new ArrayList<>();
		List<String> fail = new ArrayList<>();

		for (User user : filterUsers) {
			try {
				NotionDatabaseResult database =
						notionClientFeignAdapter.getDatabase(user.getNotionAccessToken(),
								user.getNotionDatabaseId());
				user.updateNotionPageId(database.getParent().getPage_id());
				success.add(user.getGithubOwner());
			} catch (Exception e) {
				fail.add(user.getGithubOwner());
			}
		}

		result.put("success", success);
		result.put("fail", fail);
		return result;
	}

	private User findUser(Long userId) {
		return
				userRepositoryPort
						.findById(userId)
						.orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));
	}

}
