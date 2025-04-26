package kr.co.nogibackend.domain.github.command;

public record GithubAddCollaboratorCommand(
		String owner,
		String repo,
		String username,
		String accessToken
) {

}
