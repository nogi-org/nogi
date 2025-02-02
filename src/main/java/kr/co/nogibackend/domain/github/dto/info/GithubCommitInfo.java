package kr.co.nogibackend.domain.github.dto.info;

import java.util.List;

public record GithubCommitInfo(
	String sha,
	String html_url,
	Commit commit,
	User author,
	User committer,
	List<ParentCommit> parents
) {
	// 📌 Commit 객체 (커밋 상세 정보)
	public record Commit(
		String message,
		CommitUser author,
		CommitUser committer,
		Tree tree,
		int comment_count,
		Verification verification
	) {
	}

	// 📌 CommitUser 객체 (커밋 작성자 및 커미터 정보)
	public record CommitUser(
		String name,
		String email,
		String date
	) {
	}

	// 📌 Tree 객체 (트리 정보)
	public record Tree(
		String url,
		String sha
	) {
	}

	// 📌 Verification 객체 (서명 검증 정보)
	public record Verification(
		boolean verified,
		String reason,
		String signature,
		String payload,
		String verified_at
	) {
	}

	// 📌 User 객체 (GitHub 사용자 정보)
	public record User(
		String login,
		String avatar_url,
		String html_url
	) {
	}

	// 📌 ParentCommit 객체 (부모 커밋 정보)
	public record ParentCommit(
		String url,
		String sha
	) {
	}
}