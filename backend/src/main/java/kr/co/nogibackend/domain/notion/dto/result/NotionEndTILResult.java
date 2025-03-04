package kr.co.nogibackend.domain.notion.dto.result;

public record NotionEndTILResult(
    Long userId, // 유저 ID
    String notionPageId, // 노션 페이지 ID
    String category,
    String title,
    String content
) {

}
