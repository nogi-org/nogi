package kr.co.nogibackend.interfaces.notion.dto;

import kr.co.nogibackend.domain.notion.dto.command.NotionConnectionTestCommand;

/*
  Package Name : kr.co.nogibackend.interfaces.notion.dto
  File Name    : NotionConnectionTestRequest
  Author       : superpil
  Created Date : 25. 2. 25.
  Description  :
 */
public record NotionConnectionTestRequest(
	String notionDatabaseId
	, String notionBotToken
) {

	public NotionConnectionTestCommand toCommand() {
		return new NotionConnectionTestCommand(this.notionDatabaseId, this.notionBotToken);
	}

}
