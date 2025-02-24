package kr.co.nogibackend.domain.github.dto.request;

// 1️⃣ Blob 요청 (파일을 업로드)
public record GithubCreateBlobRequest(
	String content,
	String encoding
) {
}
