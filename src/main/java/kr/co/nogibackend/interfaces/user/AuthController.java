package kr.co.nogibackend.interfaces.user;

import static kr.co.nogibackend.util.CookieUtil.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.nogibackend.application.user.UserFacade;
import kr.co.nogibackend.application.user.dto.UserFacadeCommand;
import kr.co.nogibackend.domain.user.dto.info.UserLoginByGithubInfo;
import kr.co.nogibackend.response.service.Response;
import kr.co.nogibackend.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
  Package Name : kr.co.nogibackend.interfaces.user
  File Name    : LoginController
  Author       : won taek oh
  Created Date : 25. 2. 14.
  Description  :
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final UserFacade userFacade;
	private final CookieUtil cookieUtil;

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
		UserFacadeCommand.GithubLogin userFacadeCommand =
			new UserFacadeCommand.GithubLogin(githubClientId, githubClientSecret, code);
		UserLoginByGithubInfo userLoginByGithubInfo = userFacade.loginByGithub(userFacadeCommand);

		String redirectUrl = String.format(
			"%s?requireUserInfo=%s&userId=%s&role=%s",
			afterLoginRedirectUrl,
			userLoginByGithubInfo.isRequireUserInfo(),
			userLoginByGithubInfo.getUserInfo().id(),
			userLoginByGithubInfo.getUserInfo().role()
		);

		// access token 쿠키 설정
		cookieUtil.createCookie
			(response, ACCESS_COOKIE_NAME, userLoginByGithubInfo.getAccessToken(), createAccessTokenCookieExpTime());

		// 프론트엔드 리다이렉트 주소로 이동
		response.sendRedirect(redirectUrl);
		return Response.success();
	}

	@PutMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		cookieUtil.deleteCookie(response, ACCESS_COOKIE_NAME);
		return Response.success();
	}

}
