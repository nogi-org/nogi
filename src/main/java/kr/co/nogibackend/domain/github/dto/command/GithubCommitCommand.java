package kr.co.nogibackend.domain.github.dto.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import kr.co.nogibackend.domain.notion.dto.result.NotionStartTILResult;
import kr.co.nogibackend.domain.user.NogiHistoryType;
import kr.co.nogibackend.domain.user.dto.result.UserCheckTILResult;

public record GithubCommitCommand(
	Long userId,               // 유저 ID
	String githubOwner,           // 유저 이름 (github owner)
	String githubRepository,   // 깃허브 레포지토리
	String githubBranch,       // 깃허브 브랜치
	String githubEmail,       // 깃허브 이메일
	String notionPageId,        // 노션 페이지 ID
	String notionBotToken,     // 노션 인증 토큰
	NogiHistoryType type,       // 히스토리 타입 (생성, 수정 등)
	String newCategory,         // 새로운 카테고리 (디렉토리 하위 구조)
	String newTitle,            // 새로운 제목 (파일명)
	String prevCategory,        // 이전 카테고리
	String prevTitle,           // 이전 제목
	String commitDate,          // 커밋 일자
	String content,             // markdown 파일 내용
	String githubToken,         // 깃허브 토큰
	List<ImageOfGithub> images // 이미지 정보
) {
	/**
	 📌 List<NotionStartTILResult>와 List<UserCheckTILResult>를 조합하여 List<GithubCommitCommand> 생성
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
					userCheckTILResult.userName(),
					userCheckTILResult.repository(),
					userCheckTILResult.branch(),
					userCheckTILResult.githubEmail(),
					notion.notionPageId(),
					userCheckTILResult.notionBotToken(),
					userCheckTILResult.type(),
					notion.category(),
					notion.title(),
					userCheckTILResult.prevCategory(),
					userCheckTILResult.prevTitle(),
					notion.commitDate(),
					notion.content(),
					userCheckTILResult.githubToken(),
					notion.images().stream()
						.map(image -> new ImageOfGithub(image.fileEnc64(), image.fileName(), image.filePath()))
						.collect(Collectors.toList())
				);
			})
			.collect(Collectors.toList());
	}

	private String getMarkdownFilePath() {
		return newCategory + "/" + newTitle + ".md";
	}

	private String getPrevMarkdownFilePath() {
		return prevCategory + "/" + prevTitle + ".md";
	}

	private String getMarkdownFileName() {
		return newTitle + ".md";
	}

	public Map<String, String> prepareFiles() {
		Map<String, String> fileMap = new HashMap<>();
		addMarkdownFile(fileMap);
		addImageFiles(fileMap);
		addHistoryFile(fileMap);
		return fileMap;
	}

	public String getCommitMessage() {
		return """
			%s/%s.md %s 완료
			""".formatted(newCategory, newTitle, type == NogiHistoryType.CREATE_OR_UPDATE_CONTENT ? "생성" : "수정");
	}

	private void addMarkdownFile(Map<String, String> fileMap) {
		fileMap.put(getMarkdownFilePath(), getMarkdownFileName());
	}

	private void addImageFiles(Map<String, String> fileMap) {
		images.forEach(image ->
			fileMap.put(image.getImageFilePath(newCategory), image.getImageFile())
		);
	}

	private void addHistoryFile(Map<String, String> fileMap) {
		if (type == NogiHistoryType.UPDATE_TITLE) {
			fileMap.put(getPrevMarkdownFilePath(), null);
		} else if (type == NogiHistoryType.UPDATE_CATEGORY) {
			fileMap.put(prevCategory, null);
		}
	}

	public record ImageOfGithub(
		String fileEnc64, // 이미지 파일
		String fileName,  // 이미지 파일명
		String filePath   // 이미지 파일 경로
	) {
		public String getImageFilePath(String category) {
			return category + "/" + filePath + "/" + fileName;
		}

		public String getImageFile() {
			return fileEnc64;
		}
	}
}
