package kr.co.nogibackend.infra.github.dto;

import java.util.List;

// 3️⃣ Commit 요청
public record GithubCommitRequest(
	String message,
	String tree,
	List<String> parents,
	AuthorCommitter author,
	AuthorCommitter committer
) {
	public record AuthorCommitter(String name, String email, String date) {
	}
}