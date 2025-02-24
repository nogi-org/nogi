package kr.co.nogibackend.domain.user.dto.result;

import kr.co.nogibackend.domain.user.User;

public record UserResult(
	Long id,
	User.Role role,
	String notionBotToken,
	String notionDatabaseId,
	String githubAuthToken,
	String githubRepository,
	String githubDefaultBranch,
	String githubEmail,
	String githubOwner,
	Boolean isNotificationAllowed
) {

	public static UserResult from(
		User user
	) {
		return new UserResult(
			user.getId(),
			user.getRole(),
			user.getNotionBotToken(),
			user.getNotionDatabaseId(),
			user.getGithubAuthToken(),
			user.getGithubRepository(),
			user.getGithubDefaultBranch(),
			user.getGithubEmail(),
			user.getGithubOwner(),
			user.getIsNotificationAllowed()
		);
	}

	public boolean isUnProcessableToNogi() {
		return githubRepository == null || notionDatabaseId == null || notionBotToken == null;
	}
}
