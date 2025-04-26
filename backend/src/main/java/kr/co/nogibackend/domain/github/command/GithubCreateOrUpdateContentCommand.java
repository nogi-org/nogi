package kr.co.nogibackend.domain.github.command;

public record GithubCreateOrUpdateContentCommand(
		String message,
		String content,
		GithubCommitter committer
) {

	public record GithubCommitter(
			String name,
			String email
	) {

	}
}
