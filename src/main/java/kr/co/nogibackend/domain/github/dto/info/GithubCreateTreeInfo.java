package kr.co.nogibackend.domain.github.dto.info;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubCreateTreeInfo(
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