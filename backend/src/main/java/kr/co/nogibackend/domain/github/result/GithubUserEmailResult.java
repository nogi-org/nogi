package kr.co.nogibackend.domain.github.result;

public record GithubUserEmailResult(
		String email,
		boolean primary,
		boolean verified,
		String visibility
) {

}
