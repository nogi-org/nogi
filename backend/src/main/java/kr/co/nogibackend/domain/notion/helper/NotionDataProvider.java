package kr.co.nogibackend.domain.notion.helper;

import static kr.co.nogibackend.domain.notion.dto.constant.NotionPropertyValue.STATUS_COMPLETED;

import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import kr.co.nogibackend.config.context.ExecutionResultContext;
import kr.co.nogibackend.domain.notion.NotionClient;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotionDataProvider {

  private final NotionClient notionClient;

  public void preprocessMarkdown(
      NotionInfo<NotionBlockInfo> blocks
      , String notionAccessToken
  ) {
    for (NotionBlockInfo block : blocks.getResults()) {
      try {
        switch (block.getType()) {
          case "table":
            NotionInfo<NotionBlockInfo> rows =
                this.getBlocks(notionAccessToken, block.getId(), userId);
            break;
        }
      } catch (Exception error) {
        ExecutionResultContext.addNotionPageErrorResult("Markdown 변환 중 문제가 발생했어요.", userId);
      }
    }
  }

  // 노션 페이지의 블럭을 모두 불러오기(1회 최대 100개만 가져올 수 있음)
  public NotionInfo<NotionBlockInfo> getBlocks(
      String notionBotToken
      , String parentBlockId
  ) {
    NotionInfo<NotionBlockInfo> blocks =
        notionClient.getBlocksFromParent(notionBotToken, parentBlockId);

    // hasMore 이 true 면 next_cursor 로 다음 블럭을 가져온다.
    boolean hasMore = blocks.isHas_more();
    String nextCursor = blocks.getNext_cursor();

    while (hasMore) {
      NotionInfo<NotionBlockInfo> nextBlocks =
          notionClient.getBlocksFromParent(notionBotToken, parentBlockId, nextCursor);
      blocks.getResults().addAll(nextBlocks.getResults());
      hasMore = nextBlocks.isHas_more();
      nextCursor = nextBlocks.getNext_cursor();
    }

    return blocks;
  }

  public String getImageOfBlock(URI uri) {
    byte[] imageByte = notionClient.getBlockImage(uri);
    return Base64.getEncoder().encodeToString(imageByte);
  }

  public List<NotionPageInfo> getCompletedPages(String AuthToken, String databaseId) {
    try {
      Map<String, Object> filter = NotionRequestMaker.createPageFilterEqStatus(STATUS_COMPLETED);
      return
          notionClient
              .getPagesFromDatabase(AuthToken, databaseId, filter)
              .getResults();
    } catch (Exception error) {
      return List.of();
    }
  }

  // TODO 코드 리팩터링
  public String getNotionDatabaseInfo(
      String notionAccessToken,
      String parentBlockId
  ) {
    NotionInfo<NotionBlockInfo> notionPageInfo =
        notionClient.getBlocksFromParent(notionAccessToken, parentBlockId);
    List<NotionBlockInfo> results = notionPageInfo.getResults();
    NotionBlockInfo childDatabase =
        results
            .stream()
            .filter(v -> v.getType().equals("child_database"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Notion Page 에서 Database 를 찾을 수 없습니다."));
    return childDatabase.getId();
  }

}
