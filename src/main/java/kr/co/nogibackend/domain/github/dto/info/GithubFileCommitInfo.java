package kr.co.nogibackend.domain.github.dto.info;

public record GithubFileCommitInfo(
	Content content,
	Commit commit
) {
	public record Content(
		String name,
		String path,
		String sha,
		String url,
		String html_url,
		String git_url,
		String download_url
	) {
	}

	public record Commit(
		String sha,
		String message,
		Author author,
		Author committer
	) {
	}

	public record Author(
		String name,
		String email,
		String date
	) {
	}
}
