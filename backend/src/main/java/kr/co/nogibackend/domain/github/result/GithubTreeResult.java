package kr.co.nogibackend.domain.github.result;

import java.util.List;

public record GithubTreeResult(
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
