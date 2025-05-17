package kr.co.nogibackend.domain.user.result;

import kr.co.nogibackend.domain.user.entity.User;

public record UserSinUpOrUpdateResult(
		Long id,
		User.Role role,
		String notionPageId,
		String githubRepository,
		String githubOwner,
		boolean isSinUp
) {

	public static UserSinUpOrUpdateResult from(User user, boolean isSinUp) {
		return new UserSinUpOrUpdateResult(
				user.getId(),
				user.getRole(),
				user.getNotionPageId(),
				user.getGithubRepository(),
				user.getGithubOwner(),
				isSinUp
		);
	}
}
