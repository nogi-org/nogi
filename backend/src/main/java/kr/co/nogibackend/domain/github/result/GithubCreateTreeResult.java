package kr.co.nogibackend.domain.github.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GithubCreateTreeResult(
		String sha,
		String url,
		@JsonProperty("tree") List<TreeEntry> tree,
		String truncated
) {

	public record TreeEntry(
			String path,
			String mode,
			String type,
			String size,
			String sha,
			String url
	) {

	}
}
