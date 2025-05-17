package kr.co.nogibackend.interfaces.user.request;

import kr.co.nogibackend.domain.user.command.UserUpdateCommand;

public record UserUpdateRequest(
		String githubRepository,
		String githubEmail,
		String githubOwner,
		Boolean isNotificationAllowed,
		String selfIntroduction
) {

	public UserUpdateCommand toCommand(Long id) {
		return UserUpdateCommand.builder()
				.id(id)
				.githubRepository(githubRepository)
				.githubEmail(githubEmail)
				.githubOwner(githubOwner)
				.isNotificationAllowed(isNotificationAllowed)
				.selfIntroduction(selfIntroduction)
				.build();
	}
}
