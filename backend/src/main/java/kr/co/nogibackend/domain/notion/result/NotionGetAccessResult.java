package kr.co.nogibackend.domain.notion.result;

public record NotionGetAccessResult(
    String notionAccessToken,
    String notionPageId
) {

}
