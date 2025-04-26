package kr.co.nogibackend.domain.github.command;

import java.util.List;

public record GithubCreateCommitCommand(
		String message,
		String tree,
		List<String> parents,
		AuthorCommitter author,
		AuthorCommitter committer
) {

	public record AuthorCommitter(
			String name,
			String email,
			String date
	) {

	}
}
