package kr.co.nogibackend.domain.github.result;

public record GithubUserResult(
		Long id,
		String email,
		String owner
) {

	public static GithubUserResult from(
			GithubUserDetailResult userInfo,
			GithubUserEmailResult userEmailInfo
	) {
		return new GithubUserResult(
				userInfo.id(),
				userEmailInfo.email(),
				userInfo.login()
		);
	}
}
