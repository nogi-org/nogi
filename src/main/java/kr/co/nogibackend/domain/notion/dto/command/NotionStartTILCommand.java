package kr.co.nogibackend.domain.notion.dto.command;

import kr.co.nogibackend.domain.user.dto.result.UserResult;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotionStartTILCommand {

	private Long userId;
	private String notionBotToken;
	private String notionDatabaseId;

	public static NotionStartTILCommand from(
		UserResult userResult
	) {
		return NotionStartTILCommand.builder()
			.userId(userResult.id())
			.notionBotToken(userResult.notionBotToken())
			.notionDatabaseId(userResult.notionDatabaseId())
			.build();
	}
}
