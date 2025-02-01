package kr.co.nogibackend.domain.notion.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotionStartTilCommand {
	private Long id;
	private String notionAuthToken;
	private String notionDatabaseId;
}
