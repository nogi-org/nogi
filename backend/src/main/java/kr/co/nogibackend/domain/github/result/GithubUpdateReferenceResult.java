package kr.co.nogibackend.domain.github.result;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubUpdateReferenceResult(
		String ref,
		@JsonProperty("node_id") String nodeId,
		String url,
		@JsonProperty("object") GitObject gitObject
) {

	public record GitObject(
			String type,
			String sha,
			String url
	) {

	}
}
