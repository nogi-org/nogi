package kr.co.nogibackend.domain.notion;

import static kr.co.nogibackend.domain.notion.NotionPropertyValue.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nogibackend.domain.notion.dto.command.NotionEndTilCommand;
import kr.co.nogibackend.domain.notion.dto.command.NotionStartTilCommand;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import kr.co.nogibackend.domain.notion.dto.result.NotionStartTilResult;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotionService {

	private final NotionClient notionClient;

	public void startTIL(List<NotionStartTilCommand> commands) {
	}

	public List<NotionStartTilResult> startTIL(NotionStartTilCommand command) {
		// 대기 상태 TIL 페이지 조회
		List<NotionPageInfo> pages = this.getPendingPages(
			command.getNotionAuthToken(),
			command.getNotionDatabaseId()
		);

		// 페이지의 블럭 조회
		List<NotionBlockInfo> blocks = this.getBlocksOfPages(
			command.getNotionAuthToken(), pages
		);
		System.out.println("조회된 블락 개수 : " + blocks.size());
		System.out.println("조회된 블락 : " + blocks);
		// System.out.println("요청한 페이지ID : " + page.getId() + ", 블럭의 페이지ID : " + body.getResults()
		// 	.get(0)
		// 	.getParent()
		// 	.getPage_id());
		// System.out.println("블럭 !!! ::: " + body.getResults().size());
		// System.out.println("블럭 !!! ::: " + body);

		// 블럭 ->  마크다운 가공
		// 생성 또는 수정인지 판단
		// dto 로 반환
		return null;
	}

	public void endTIL(List<NotionEndTilCommand> users) {
	}

	public void endTIL(NotionEndTilCommand user) {
	}

	public List<NotionBlockInfo> getBlocksOfPages(String notionAuthToken, List<NotionPageInfo> pages) {
		List<NotionBlockInfo> blocks = new ArrayList<>();
		for (NotionPageInfo page : pages) {
			// boolean hasMore = true;
			NotionInfo<NotionBlockInfo> body =
				notionClient
					.getBlocksFromPage(notionAuthToken, page.getId())
					.getBody();

			// hasMore = body.isHas_more() ? true : false;
			System.out.println("has more : " + body.isHas_more());

			// while (hasMore) {
			// 	NotionResponse<NotionBlockResponse> body1 =
			// 		notionClient
			// 			.getBlocksFromPage(user.getNotionAuthToken(), page.getId())
			// 			.getBody();
			// 	blocks.addAll(body1.getResults());
			// 	hasMore = body1.isHas_more() ? true : false;
			// }
		}
		return blocks;
	}

	private NotionInfo<NotionBlockInfo> getBlocks(String authToken, String pageId) {
		try {
			return
				notionClient
					.getBlocksFromPage(authToken, pageId)
					.getBody();
		} catch (Exception error) {
			// todo: 에러 핸들링 하기(notice 역할 만들고 작업하기)
			System.out.println(error.getMessage());
			return NotionInfo.empty();
		}
	}

	public List<NotionPageInfo> getPendingPages(
		String authToken,
		String databaseId
	) {
		try {
			Map<String, Object> filter = NotionRequestMaker.filterStatusEq(STATUS_PENDING);
			return
				notionClient
					.getPagesFromDatabase(authToken, databaseId, filter)
					.getBody()
					.getResults();
		} catch (Exception error) {
			// todo: 에러 핸들링 하기(notice 역할 만들고 작업하기)
			System.out.println(error.getMessage());
			return Collections.emptyList();
		}
	}

}
