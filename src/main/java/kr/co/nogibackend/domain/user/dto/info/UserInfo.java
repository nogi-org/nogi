package kr.co.nogibackend.domain.user.dto.info;

import kr.co.nogibackend.domain.user.User;

public record UserInfo(
	Long id,
	String notionAuthToken,
	String notionDatabaseId,
	String githubAuthToken,
	String githubRepository,
	String githubDefaultBranch,
	String githubEmail,
	String githubOwner
) {

	public static UserInfo from(
		User user
	) {
		return new UserInfo(
			user.getId(),
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

