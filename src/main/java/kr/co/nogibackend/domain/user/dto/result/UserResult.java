package kr.co.nogibackend.domain.user.dto.result;

public record UserResult(
	Long id,
	String notionAuthToken,
	String notionDatabaseId,
	String githubAuthToken,
	String githubRepository,
	String githubDefaultBranch,
	String githubEmail,
	String githubOwner
) {
}
