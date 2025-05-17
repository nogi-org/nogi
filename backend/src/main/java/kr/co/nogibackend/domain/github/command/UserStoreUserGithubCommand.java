package kr.co.nogibackend.domain.github.command;

public record UserStoreUserGithubCommand(
		String accessToken,
		String email,
		String owner
) {

}
