package kr.co.nogibackend.domain.github.dto.info;

public record GithubUserEmailInfo(
	String email,
	boolean primary,
	boolean verified,
	String visibility
) {
}
