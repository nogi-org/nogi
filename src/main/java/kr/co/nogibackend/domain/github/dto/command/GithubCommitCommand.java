package kr.co.nogibackend.domain.github.dto.command;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import kr.co.nogibackend.domain.notion.dto.result.NotionStartTILResult;
import kr.co.nogibackend.domain.user.NogiHistoryType;
import kr.co.nogibackend.domain.user.dto.result.UserCheckTILResult;

/**
 * 파일 올리는 관련
 * https://docs.github.com/ko/rest/repos/contents?apiVersion=2022-11-28
 */
public record GithubCommitCommand(
	Long userId,               // 유저 ID
	String userName,           // 유저 이름 (github owner)
	String notionPageId,        // 노션 페이지 ID
	NogiHistoryType type,       // 히스토리 타입 (생성, 수정 등)
	String newCategory,         // 새로운 카테고리 (디렉토리 하위 구조)
	String newTitle,            // 새로운 제목 (파일명)
	String prevCategory,        // 이전 카테고리
	String prevTitle,           // 이전 제목
	String commitDate,          // 커밋 일자
	String content,             // markdown 파일 내용
	String githubToken,         // 깃허브 토큰
	boolean isSuccess,          // 성공 여부
	List<NotionStartTILResult.ImageOfNotionBlock> images // 이미지 정보
) {
	/**
	 * 📌 List<NotionStartTILResult>와 List<UserCheckTILResult>를 받아서
	 * notionPageId 기준으로 매핑하여 List<GithubCommitCommand> 생성
	 */
	public static List<GithubCommitCommand> of(
		List<NotionStartTILResult> notionResults,
		List<UserCheckTILResult> userCheckResults
	) {
		// ✅ userCheckResults를 notionPageId 기준으로 매핑 (빠른 조회를 위해 Map 사용)
		Map<String, UserCheckTILResult> userCheckMap = userCheckResults.stream()
			.collect(Collectors.toMap(UserCheckTILResult::notionPageId, Function.identity()));

		// ✅ notionResults를 기반으로 GithubCommitCommand 리스트 생성
		return notionResults.stream()
			.map(notion -> {
				UserCheckTILResult userCheckTILResult = userCheckMap.get(notion.notionPageId());

				return new GithubCommitCommand(
					notion.userId(),
					useerCheckTILResult.userName(),
					notion.notionPageId(),
					userCheckTILResult.type(),
					notion.category(),
					notion.title(),
					userCheckTILResult.prevCategory(),
					userCheckTILResult.prevTitle(),
					notion.commitDate(),
					notion.content(),
					userCheckTILResult.githubToken(),
					userCheckTILResult.isSuccess() && notion.isSuccess(),
					notion.images()
				);
			})
			.collect(Collectors.toList());
	}
}