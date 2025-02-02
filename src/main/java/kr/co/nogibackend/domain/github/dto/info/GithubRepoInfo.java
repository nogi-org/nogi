package kr.co.nogibackend.domain.github.dto.info;

public record GithubRepoInfo(
	Long id,
	String name,
	GithubOwnerInfo owner

) {
	public record GithubOwnerInfo(
		String login,
		Long id
	) {
	}
}
