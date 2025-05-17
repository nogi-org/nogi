package kr.co.nogibackend.domain.github.command;

public record GithubAccessTokenCommand(
		String client_id,
		String client_secret,
		String code,
		String scope
) {

}
