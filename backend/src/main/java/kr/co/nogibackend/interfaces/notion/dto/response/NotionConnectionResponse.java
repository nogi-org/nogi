package kr.co.nogibackend.interfaces.notion.dto.response;

public record NotionConnectionResponse(
		boolean isConnection,
		String message
) {

}
