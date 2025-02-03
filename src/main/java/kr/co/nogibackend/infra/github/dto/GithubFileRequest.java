package kr.co.nogibackend.infra.github.dto;

public record GithubFileRequest(
	String message,
	String content,  // Base64 인코딩된 파일 내용
	String sha,      // 기존 파일이 있으면 SHA 필요
	String branch    // 브랜치명 (기본값: main)
) {
}
