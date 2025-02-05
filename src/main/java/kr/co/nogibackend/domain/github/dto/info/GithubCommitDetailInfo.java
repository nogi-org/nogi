package kr.co.nogibackend.domain.github.dto.info;

import java.util.List;

public record GithubCommitDetailInfo(
	String sha,
	String node_id,
	String message,
	GithubCommitAuthor author,
	GithubCommitAuthor committer,
	GithubCommitTreeInfo tree, // ✅ 기존에 없던 Tree 정보 추가
	List<GithubCommitTree> parents
) {
	public record GithubCommitAuthor(String name, String email, String date) {
	}

	public record GithubCommitTree(String sha, String url) {
	}

	public record GithubCommitTreeInfo(String sha, String url) {
	} // ✅ 추가된 서브 레코드
}