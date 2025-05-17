package kr.co.nogibackend.domain.github.command;

// 4️⃣ Update Reference 요청 (브랜치 업데이트)
public record GithubUpdateReferenceCommand(
		String sha,
		boolean force
) {

}
