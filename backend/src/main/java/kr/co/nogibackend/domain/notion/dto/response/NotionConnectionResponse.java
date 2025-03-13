package kr.co.nogibackend.domain.notion.dto.response;

public record NotionConnectionResponse(
    boolean isConnection,
    String message
) {

}
