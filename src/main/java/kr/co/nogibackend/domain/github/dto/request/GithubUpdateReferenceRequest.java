package kr.co.nogibackend.domain.github.dto.request;

// 4️⃣ Update Reference 요청 (브랜치 업데이트)
public record GithubUpdateReferenceRequest(
	String sha,
	boolean force
) {
}