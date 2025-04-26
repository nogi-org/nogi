package kr.co.nogibackend.domain.github.result;

public record GithubCommitResult(
		Long userId,
		String notionPageId,
		String notionBotToken,
		String category,
		String title,
		String content,
		boolean isSuccess
) {

}
