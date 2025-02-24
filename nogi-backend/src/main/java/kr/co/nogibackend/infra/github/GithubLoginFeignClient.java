package kr.co.nogibackend.infra.github;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import kr.co.nogibackend.domain.github.dto.info.GithubOauthAccessTokenInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubOAuthAccessTokenRequest;

/*
  Package Name : kr.co.nogibackend.infra.github
  File Name    : GithubLoginFeignClient
  Author       : won taek oh
  Created Date : 25. 2. 9.
  Description  : GIT API를 호출하기 위한 Feign Client
 */
@FeignClient(name = "GithubLoginFeignClient", url = "https://github.com")
public interface GithubLoginFeignClient {

	/*
	➡️ Access Token 가져오기
	doc: https://docs.github.com/ko/developers/apps/authorizing-oauth-apps#web-application-flow
	 */
	@PostMapping("/login/oauth/access_token")
	GithubOauthAccessTokenInfo getAccessToken(
		@RequestBody GithubOAuthAccessTokenRequest request
	);
}
