package kr.co.nogibackend.application.user.dto;

public class UserFacadeCommand {

	public record GithubLogin(
		String clientId,
		String clientSecret,
		String code
	) {

	}
}
