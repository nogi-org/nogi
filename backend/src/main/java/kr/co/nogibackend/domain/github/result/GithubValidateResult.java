package kr.co.nogibackend.domain.github.result;

import java.util.List;
import kr.co.nogibackend.domain.user.result.UserReslut;

public record GithubValidateResult(
		UserReslut user,
		GithubUserDetailResult githubUserDetailResult,
		List<GithubUserEmailResult> githubEmails,
		List<GithubRepoResult> githubRepositories
) {

}
