package kr.co.nogibackend.infra.github.dto;

public record GithubFileDeleteRequest(
	String message,   // 커밋 메시지
	String sha,       // 삭제할 파일의 SHA 값
	GithubCommitter committer, // 커밋 작성자 정보
	String branch     // 브랜치 이름 (기본: main)
) {
	public record GithubCommitter(
		String name,
		String email
	) {
	}
}
