package kr.co.nogibackend.domain.notion.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotionStartTILCommand {

	private Long userId;
	private String notionAuthToken;
	private String notionDatabaseId;

}
