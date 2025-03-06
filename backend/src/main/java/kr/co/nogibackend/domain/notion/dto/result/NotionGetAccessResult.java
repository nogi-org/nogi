package kr.co.nogibackend.domain.notion.dto.result;

public record NotionGetAccessResult(
    String notionAccessToken,
    String notionPageId
) {

}
