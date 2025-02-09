package kr.co.nogibackend.domain.github.dto.info;

import java.util.List;

public record GithubTreeInfo(
	String sha,
	String url,
	List<GithubTreeNode> tree
) {
	public record GithubTreeNode(
		String path,
		String mode,
		String type,
		String sha,
		String url
	) {
	}
}