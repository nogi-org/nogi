package kr.co.nogibackend.interfaces.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.nogibackend.application.user.UserFacade;
import kr.co.nogibackend.application.user.dto.UserFacadeCommand;
import kr.co.nogibackend.domain.user.dto.info.UserLoginByGithubInfo;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;

/*
  Package Name : kr.co.nogibackend.interfaces.user
  File Name    : LoginController
  Author       : won taek oh
  Created Date : 25. 2. 14.
  Description  :
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

	private final UserFacade userFacade;

	@Value("${github.client.id}")
	private String githubClientId;

	@Value("${github.client.secret}")
	private String githubClientSecret;

	@Value("${github.after-login-redirect-uri}")
	private String afterLoginRedirectUrl;

	/**
	 github login 페이지로 이동할 수 있는 url을 반환
	 public_repo, user:email 권한을 요청
	 */
	@GetMapping("github/auth-url")
	public ResponseEntity<?> getGithubAuthUrl() {
		return Response.success(
			"https://github.com/login/oauth/authorize?client_id=" + githubClientId + "&scope=user:email,public_repo"
		);
	}

	/**
	 Authorization callback URL 을 통해 code 를 받아서 처리
	 */
	@GetMapping("login/code/github")
	public ResponseEntity<?> loginByGithub(
		String code,
		HttpServletResponse response
	) throws IOException {

		UserLoginByGithubInfo userLoginByGithubInfo = userFacade.loginByGithub(
			new UserFacadeCommand.GithubLogin(githubClientId, githubClientSecret, code)
		);

		String redirectUrl = String.format(
			"%s/oauth2/redirect?accessToken=%s&requireUserInfo=%s&userId=%s",
			afterLoginRedirectUrl,
			userLoginByGithubInfo.getAccessToken(),
			userLoginByGithubInfo.isRequireUserInfo(),
			userLoginByGithubInfo.getUserInfo().id()
		);

		// 프론트엔드 홈으로 리디렉트
		response.sendRedirect(redirectUrl);
		return ResponseEntity.ok().build();
	}

}
