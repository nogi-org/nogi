package kr.co.nogibackend.domain.github.dto.info;

public record GithubFileDeleteInfo(
	GithubCommitInfo commit // 삭제 후 생성된 커밋 정보
) {
	public record GithubCommitInfo(
		String sha,
		String url,
		String message
	) {
	}
}
