package kr.co.nogibackend.domain.user.result;

import kr.co.nogibackend.domain.user.entity.User;
import org.springframework.util.StringUtils;

public record UserResult(
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
		Boolean isNotificationAllowed
) {

	public static UserResult from(
			User user
	) {
		return new UserResult(
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
				user.getIsNotificationAllowed()
		);
	}

	public static UserResult from(UserReslut userReslut) {
		return new UserResult(
				userReslut.id(),
				userReslut.role(),
				userReslut.notionAccessToken(),
				userReslut.notionPageId(),
				userReslut.notionDatabaseId(),
				userReslut.githubAuthToken(),
				userReslut.githubRepository(),
				userReslut.githubDefaultBranch(),
				userReslut.githubEmail(),
				userReslut.githubOwner(),
				userReslut.isNotificationAllowed()
		);
	}

	public boolean isUnProcessableToNogi() {
		return !StringUtils.hasText(githubRepository)
				|| !StringUtils.hasText(notionDatabaseId)
				|| !StringUtils.hasText(notionAccessToken);
	}

	public boolean isNotionDatabaseIdEmpty() {
		return StringUtils.hasText(notionAccessToken)
				&& StringUtils.hasText(notionPageId)
				&& !StringUtils.hasText(notionDatabaseId);
	}
}
