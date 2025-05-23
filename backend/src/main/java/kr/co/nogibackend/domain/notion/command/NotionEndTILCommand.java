package kr.co.nogibackend.domain.notion.command;

import kr.co.nogibackend.domain.github.result.GithubCommitResult;

public record NotionEndTILCommand(
		Long userId,// 유저 ID
		String notionBotToken, // notion auth token
		String notionPageId, // notion page id
		String category,// 디렉터리 하위 구조
		String title,// 제목(ex 파일명.md 에서 파일명으로 사용할 값)
		String content,
		boolean isSuccess// 성공여부
) {

	public static NotionEndTILCommand from(
			GithubCommitResult githubCommitResult
	) {
		return new NotionEndTILCommand(
				githubCommitResult.userId(),
				githubCommitResult.notionBotToken(),
				githubCommitResult.notionPageId(),
				githubCommitResult.category(),
				githubCommitResult.title(),
				githubCommitResult.content(),
				githubCommitResult.isSuccess() // 실패 케이스는 따로 Context 에 저장
		);
	}
}
