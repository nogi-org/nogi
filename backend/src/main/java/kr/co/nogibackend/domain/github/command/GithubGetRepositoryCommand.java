package kr.co.nogibackend.domain.github.command;

public record GithubGetRepositoryCommand(
		String owner,
		String repoName,
		String token
) {

}
