package kr.co.nogibackend.domain.notion.dto.command;

/*
  Package Name : kr.co.nogibackend.domain.notion.dto.command
  File Name    : NotionConnectionTestCommand
  Author       : superpil
  Created Date : 25. 2. 25.
  Description  :
 */
public record NotionConnectionTestCommand(
	String notionDatabaseId
	, String notionBotToken
) {
}
