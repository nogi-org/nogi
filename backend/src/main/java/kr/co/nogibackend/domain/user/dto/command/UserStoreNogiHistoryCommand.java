package kr.co.nogibackend.domain.user.dto.command;

import kr.co.nogibackend.domain.notion.dto.result.NotionEndTILResult;

public record UserStoreNogiHistoryCommand(
    Long userId, // 유저 ID
    String notionPageId, // 노션 페이지 ID
    String category,
    String title,
    String content
) {

  public static UserStoreNogiHistoryCommand from(
      NotionEndTILResult notionEndTILResult
  ) {
    return new UserStoreNogiHistoryCommand(
        notionEndTILResult.userId(),
        notionEndTILResult.notionPageId(),
        notionEndTILResult.category(),
        notionEndTILResult.title(),
        notionEndTILResult.content()
    );
  }
}
