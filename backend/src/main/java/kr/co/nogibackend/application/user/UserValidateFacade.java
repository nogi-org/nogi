package kr.co.nogibackend.application.user;

import java.util.List;
import kr.co.nogibackend.domain.github.port.GithubClientPort;
import kr.co.nogibackend.domain.github.result.GithubRepoResult;
import kr.co.nogibackend.domain.github.result.GithubUserDetailResult;
import kr.co.nogibackend.domain.github.result.GithubUserEmailResult;
import kr.co.nogibackend.domain.github.result.GithubValidateResult;
import kr.co.nogibackend.domain.user.result.UserReslut;
import kr.co.nogibackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidateFacade {

	private final UserService userService;
	private final GithubClientPort githubClientPort;

	public GithubValidateResult validateGithub(Long userId) {
		UserReslut userReslut = userService.findUserById(userId);

		GithubUserDetailResult githubInfo = githubClientPort.getUserInfo(userReslut.githubAuthToken());

		List<GithubUserEmailResult> githubEmails = githubClientPort.getUserEmails(
				userReslut.githubAuthToken());

		List<GithubRepoResult> githubRepositories = githubClientPort.getUserRepositories(
				userReslut.githubAuthToken());

		return new GithubValidateResult(userReslut, githubInfo, githubEmails, githubRepositories);
	}

}
