package kr.co.nogibackend.domain.github.dto.info;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubGetCommitObjectInfo(
	String sha,
	@JsonProperty("node_id") String nodeId,
	String url,
	@JsonProperty("html_url") String htmlUrl,
	GitUser author,
	GitUser committer,
	Tree tree,
	String message,
	List<Parent> parents,
	Verification verification
) {

	public record GitUser(
		String date,
		String email,
		String name
	) {
	}

	public record Tree(
		String sha,
		String url
	) {
	}

	public record Parent(
		String sha,
		String url,
		@JsonProperty("html_url") String htmlUrl
	) {
	}

	public record Verification(
		boolean verified,
		String reason,
		// signature, payload, verified_at 는 null 가능하므로 String 타입으로 처리
		String signature,
		String payload,
		@JsonProperty("verified_at") String verifiedAt
	) {
	}
}