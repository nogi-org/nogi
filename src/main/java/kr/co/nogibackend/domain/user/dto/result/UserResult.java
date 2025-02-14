package kr.co.nogibackend.domain.user.dto.result;

import kr.co.nogibackend.domain.user.User;

public record UserResult(
	Long id,
	User.Role role,
	String notionAuthToken,
	String notionDatabaseId,
	String githubAuthToken,
	String githubRepository,
	String githubDefaultBranch,
	String githubEmail,
	String githubOwner
) {

	public static UserResult from(
		User user
	) {
		return new UserResult(
			user.getId(),
			user.getRole(),
			user.getNotionAuthToken(),
			user.getNotionDatabaseId(),
			user.getGithubAuthToken(),
			user.getGithubRepository(),
			user.getGithubDefaultBranch(),
			user.getGithubEmail(),
			user.getGithubOwner()
		);
	}
}
