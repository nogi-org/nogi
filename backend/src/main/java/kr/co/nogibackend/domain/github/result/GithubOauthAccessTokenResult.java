package kr.co.nogibackend.domain.github.result;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubOauthAccessTokenResult(
		@JsonProperty("access_token")
		String accessToken
) {

}
