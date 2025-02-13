package kr.co.nogibackend.application.notion;

public class UserFacadeCommand {

	public record GithubLogin(
		String clientId,
		String clientSecret,
		String code
	) {

	}
}
