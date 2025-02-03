package kr.co.nogibackend.domain.github.dto.info;

public record GithubFileShaInfo(
	String name,       // 파일 이름
	String path,       // 파일 경로
	String sha,        // 최신 SHA 값
	String url,        // GitHub API URL
	String download_url // 다운로드 URL
) {
}
