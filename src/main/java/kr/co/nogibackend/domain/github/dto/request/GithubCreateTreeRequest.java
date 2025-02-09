package kr.co.nogibackend.domain.github.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubCreateTreeRequest(
	String owner,
	@JsonProperty("base_tree")
	String baseTree,
	List<TreeEntry> tree
) {
	public record TreeEntry(
		String path,
		String mode,
		String type,
		String sha
	) {
	}
}