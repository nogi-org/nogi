package kr.co.nogibackend.infra.github.dto;

// 4️⃣ Update Reference 요청 (브랜치 업데이트)
public record GithubUpdateReferenceRequest(
	String sha
) {
}