package kr.co.nogibackend.infra.github.dto;

import java.util.List;

// 2️⃣ Git Tree 요청
public record GithubTreeRequest(
	String base_tree,
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