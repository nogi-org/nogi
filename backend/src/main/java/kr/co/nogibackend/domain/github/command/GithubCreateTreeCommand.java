package kr.co.nogibackend.domain.github.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GithubCreateTreeCommand(
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
