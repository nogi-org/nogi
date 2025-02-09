package kr.co.nogibackend.domain.user.dto.command;

public record UserUpdateNogiHistoryCommand(
	String notionPageId,
	String category,
	String title
) {
}
