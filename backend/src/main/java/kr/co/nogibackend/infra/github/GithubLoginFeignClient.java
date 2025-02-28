package kr.co.nogibackend.infra.github;

import kr.co.nogibackend.domain.github.dto.info.GithubOauthAccessTokenInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubOAuthAccessTokenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
