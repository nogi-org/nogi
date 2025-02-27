package kr.co.nogibackend.domain.github.dto.info;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubOauthAccessTokenInfo(
    @JsonProperty("access_token")
    String accessToken
) {

}
