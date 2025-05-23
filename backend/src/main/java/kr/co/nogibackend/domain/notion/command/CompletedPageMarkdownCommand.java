package kr.co.nogibackend.domain.notion.command;

import kr.co.nogibackend.domain.user.result.UserResult;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompletedPageMarkdownCommand {

	private Long userId;
	private String githubOwner;
	private String notionBotToken;
	private String notionDatabaseId;

	public static CompletedPageMarkdownCommand from(
			UserResult userResult
	) {
		return
				CompletedPageMarkdownCommand
						.builder()
						.userId(userResult.id())
						.githubOwner(userResult.githubOwner())
						.notionBotToken(userResult.notionAccessToken())
						.notionDatabaseId(userResult.notionDatabaseId())
						.build();
	}
}
