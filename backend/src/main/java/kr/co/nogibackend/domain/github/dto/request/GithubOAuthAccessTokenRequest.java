package kr.co.nogibackend.domain.github.dto.request;

public record GithubOAuthAccessTokenRequest(
    String client_id,
    String client_secret,
    String code,
    String scope
) {

}
