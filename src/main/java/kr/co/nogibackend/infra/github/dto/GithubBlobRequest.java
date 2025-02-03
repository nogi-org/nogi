package kr.co.nogibackend.infra.github.dto;

// 1️⃣ Blob 요청 (파일을 업로드)
public record GithubBlobRequest(
	String content,
	String encoding
) {
}
