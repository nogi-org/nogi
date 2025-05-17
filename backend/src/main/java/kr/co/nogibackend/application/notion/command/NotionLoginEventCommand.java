package kr.co.nogibackend.application.notion.command;

public record NotionLoginEventCommand(
		Long userId,
		String notionAccessToken,
		String notionPageId
) {

}
