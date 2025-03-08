package kr.co.nogibackend.application.notion.dto;

public record NotionLoginEventCommand(
    Long userId,
    String notionAccessToken,
    String notionPageId
) {

}
