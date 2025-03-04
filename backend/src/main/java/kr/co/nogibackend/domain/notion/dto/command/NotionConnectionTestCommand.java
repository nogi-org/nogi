package kr.co.nogibackend.domain.notion.dto.command;

public record NotionConnectionTestCommand(
    String notionDatabaseId
    , String notionBotToken
) {

}
