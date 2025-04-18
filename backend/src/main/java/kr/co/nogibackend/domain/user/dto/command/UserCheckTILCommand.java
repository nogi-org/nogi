package kr.co.nogibackend.domain.user.dto.command;

import kr.co.nogibackend.domain.notion.result.CompletedPageMarkdownResult;

public record UserCheckTILCommand(
    Long userId,// 유저 ID
    String notionPageId,// 노션 페이지 ID
    String category,// 디렉터리 하위 구조
    String title// md 파일 제목
) {

  public static UserCheckTILCommand from(
      CompletedPageMarkdownResult completedPageMarkdownResult
  ) {
    return new UserCheckTILCommand(
        completedPageMarkdownResult.userId(),
        completedPageMarkdownResult.notionPageId(),
        completedPageMarkdownResult.category(),
        completedPageMarkdownResult.title()
    );
  }
}
