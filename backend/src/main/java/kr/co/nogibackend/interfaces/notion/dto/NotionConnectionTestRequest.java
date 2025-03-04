package kr.co.nogibackend.interfaces.notion.dto;

import kr.co.nogibackend.domain.notion.dto.command.NotionConnectionTestCommand;

public record NotionConnectionTestRequest(
    String notionDatabaseId
    , String notionBotToken
) {

  public NotionConnectionTestCommand toCommand() {
    return new NotionConnectionTestCommand(this.notionDatabaseId, this.notionBotToken);
  }

}
