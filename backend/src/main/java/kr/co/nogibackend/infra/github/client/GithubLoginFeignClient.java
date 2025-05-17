package kr.co.nogibackend.infra.github.client;

import kr.co.nogibackend.domain.github.command.GithubOAuthAccessTokenCommand;
import kr.co.nogibackend.domain.github.result.GithubOauthAccessTokenResult;
import kr.co.nogibackend.global.config.openfeign.GitHubFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "GithubLoginFeignClient", url = "https://github.com", configuration = GitHubFeignClientConfig.class)
public interface GithubLoginFeignClient {

	/*
	➡️ Access Token 가져오기
	doc: https://docs.github.com/ko/developers/apps/authorizing-oauth-apps#web-application-flow
	 */
	@PostMapping("/login/oauth/access_token")
	GithubOauthAccessTokenResult getAccessToken(
			@RequestBody GithubOAuthAccessTokenCommand request
	);
}
