package kr.co.nogibackend.interfaces.notion.response;

public record NotionConnectionResponse(
		boolean isConnection,
		String message
) {

}
