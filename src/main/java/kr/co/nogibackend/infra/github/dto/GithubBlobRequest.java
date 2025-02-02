package kr.co.nogibackend.infra.github.dto;

import java.util.Base64;

// 1️⃣ Blob 요청 (파일을 업로드)
public record GithubBlobRequest(
	String content,
	String encoding
) {
	public static GithubBlobRequest of(String content) {
		return new GithubBlobRequest(Base64.getEncoder().encodeToString(content.getBytes()), "base64");
	}
}
