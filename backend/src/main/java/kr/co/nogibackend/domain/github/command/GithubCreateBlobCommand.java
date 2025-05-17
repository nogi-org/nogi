package kr.co.nogibackend.domain.github.command;

// 1️⃣ Blob 요청 (파일을 업로드)
public record GithubCreateBlobCommand(
		String content,
		String encoding
) {

}
