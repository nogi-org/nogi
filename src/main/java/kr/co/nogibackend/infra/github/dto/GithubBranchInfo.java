package kr.co.nogibackend.infra.github.dto;

public record GithubBranchInfo(
	String name,
	Commit commit
) {
	public record Commit(
		String sha
	) {
	}
}