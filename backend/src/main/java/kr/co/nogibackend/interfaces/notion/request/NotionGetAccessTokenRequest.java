package kr.co.nogibackend.interfaces.notion.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NotionGetAccessTokenRequest(
		@JsonProperty("grant_type")
		String grantType,
		String code,
		@JsonProperty("redirect_uri")
		String redirectUri
) {

	public static NotionGetAccessTokenRequest of(String code, String redirectUri) {
		return new NotionGetAccessTokenRequest("authorization_code", code, redirectUri);
	}
}
