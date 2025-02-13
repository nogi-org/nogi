package kr.co.nogibackend.application.user;

import org.springframework.stereotype.Service;

import kr.co.nogibackend.application.notion.UserFacadeCommand;
import kr.co.nogibackend.domain.github.GithubService;
import kr.co.nogibackend.domain.github.dto.info.GithubOauthAccessTokenInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubOAuthAccessTokenRequest;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.domain.user.dto.command.UserUpdateCommand;
import lombok.RequiredArgsConstructor;

/*
  Package Name : kr.co.nogibackend.application.user
  File Name    : UserFacade
  Author       : won taek oh
  Created Date : 25. 2. 13.
  Description  :
 */
@Service
@RequiredArgsConstructor
public class UserFacade {

	private final GithubService githubService;
	private final UserService userService;

	public void loginByGithub(UserFacadeCommand.GithubLogin command) {
		// 1. access token 가져오기
		GithubOauthAccessTokenInfo accessToken = githubService.getAccessToken(
			new GithubOAuthAccessTokenRequest(
				command.clientId(),
				command.clientSecret(),
				command.code(),
				"public_repo"
			)
		);

		// 2. user 정보 가져오기
		GithubUserInfo userInfo = githubService.getUserInfo(accessToken.accessToken());

		// 3. user 정보 저장하기
		userService.storeUserGithubInfo(UserUpdateCommand.builder().build());

		// 4. access toekn 발급하기(nogi token)
	}
}
