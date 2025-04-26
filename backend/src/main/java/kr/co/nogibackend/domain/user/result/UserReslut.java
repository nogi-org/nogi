package kr.co.nogibackend.domain.user.result;

import kr.co.nogibackend.domain.user.entity.User;

public record UserReslut(
		Long id,
		User.Role role,
		String notionAccessToken,
		String notionPageId,
		String notionDatabaseId,
		String githubAuthToken,
		String githubRepository,
		String githubDefaultBranch,
		String githubEmail,
		String githubOwner,
		Boolean isNotificationAllowed,
		String selfIntroduction
) {

	public static UserReslut from(
			User user
	) {
		return new UserReslut(
				user.getId(),
				user.getRole(),
				user.getNotionAccessToken(),
				user.getNotionPageId(),
				user.getNotionDatabaseId(),
				user.getGithubAuthToken(),
				user.getGithubRepository(),
				user.getGithubDefaultBranch(),
				user.getGithubEmail(),
				user.getGithubOwner(),
				user.getIsNotificationAllowed(),
				user.getSelfIntroduction()
		);
	}
}
