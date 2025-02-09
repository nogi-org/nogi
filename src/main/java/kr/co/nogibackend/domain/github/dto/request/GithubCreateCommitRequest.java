package kr.co.nogibackend.domain.github.dto.request;

import java.util.List;

public record GithubCreateCommitRequest(
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