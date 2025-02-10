package kr.co.nogibackend.domain.github.dto.result;

public record GithubCommitResult(
	Long userId,
	String notionPageId,
	String notionAuthToken,
	String category,
	String title
) {
}
