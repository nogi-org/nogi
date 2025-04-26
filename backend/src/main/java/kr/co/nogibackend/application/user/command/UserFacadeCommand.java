package kr.co.nogibackend.application.user.command;

import java.nio.charset.StandardCharsets;

public class UserFacadeCommand {

	public record GithubLogin(
			String clientId,
			String clientSecret,
			String code
	) {

	}

	public record ValidateRepositoryName(
			Long userId,
			String repositoryName
	) {

	}

	public record NotionLogin(
			Long userId,
			String notionClientId,
			String notionClientSecret,
			String notionRedirectUri,
			String code
	) {

		public String getBasicToken() {
			String credentials = notionClientId + ":" + notionClientSecret;
			return "Basic " + java.util.Base64.getEncoder()
					.encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
		}
	}
}
