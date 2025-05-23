package kr.co.nogibackend.interfaces.auth.controller;

import static kr.co.nogibackend.global.util.CookieUtils.ACCESS_COOKIE_NAME;
import static kr.co.nogibackend.global.util.CookieUtils.createAccessTokenCookieExpTime;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import kr.co.nogibackend.application.user.UserLoginFacade;
import kr.co.nogibackend.application.user.command.UserFacadeCommand;
import kr.co.nogibackend.domain.user.result.UserLoginByGithubResult;
import kr.co.nogibackend.global.response.service.Response;
import kr.co.nogibackend.global.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GithubAuthControllerV1 {

	private final UserLoginFacade userLoginFacade;

	@Value("${github.client.id}")
	private String githubClientId;

	@Value("${github.client.secret}")
	private String githubClientSecret;

	@Value("${github.after-login-redirect-uri}")
	private String afterLoginRedirectUrl;

	/**
	 * github login 페이지로 이동할 수 있는 url을 반환 public_repo, user:email 권한을 요청
	 */
	@GetMapping("/v1/github/auth-url")
	public ResponseEntity<?> getGithubAuthUrl() {
		return Response.success(
				"https://github.com/login/oauth/authorize?client_id=" + githubClientId
						+ "&scope=user:email,public_repo"
		);
	}

	/**
	 * Authorization callback URL 을 통해 code 를 받아서 처리
	 */
	@GetMapping("login/code/github")
	public ResponseEntity<?> loginByGithub(
			String code,
			HttpServletResponse response
	) throws IOException {
		UserFacadeCommand.GithubLogin userFacadeCommand =
				new UserFacadeCommand.GithubLogin(githubClientId, githubClientSecret, code);
		UserLoginByGithubResult userLoginByGithubResult = userLoginFacade.loginByGithub(
				userFacadeCommand);

		String redirectUrl = String.format(
				"%s?isRequireNotionInfo=%s&isRequireGithubInfo=%s&userId=%s&role=%s&isSuccess=%s&message=%s&type=GITHUB",
				afterLoginRedirectUrl,
				userLoginByGithubResult.isRequireNotionInfo(),
				userLoginByGithubResult.isRequireGithubInfo(),
				userLoginByGithubResult.getUserId(),
				userLoginByGithubResult.getRole(),
				userLoginByGithubResult.isSuccess(),
				URLEncoder.encode(userLoginByGithubResult.getMessage(), StandardCharsets.UTF_8)
		);

		// 성공일 경우 access token 쿠키 설정
		if (userLoginByGithubResult.isSuccess()) {
			CookieUtils.createCookie(
					response,
					ACCESS_COOKIE_NAME,
					userLoginByGithubResult.getAccessToken(),
					createAccessTokenCookieExpTime(),
					true
			);
		}

		// 프론트엔드 리다이렉트 주소로 이동
		response.sendRedirect(redirectUrl);
		return Response.success();
	}

	@PutMapping("/v1/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		CookieUtils.deleteCookie(response, ACCESS_COOKIE_NAME);
		return Response.success();
	}

}
