package kr.co.nogibackend.domain.notion.helper;

import static kr.co.nogibackend.domain.notion.constant.NotionPropertyValue.STATUS_COMPLETED;
import static kr.co.nogibackend.domain.notion.info.NotionBlockInfo.BULLETED_LIST;
import static kr.co.nogibackend.domain.notion.info.NotionBlockInfo.IMAGE;
import static kr.co.nogibackend.domain.notion.info.NotionBlockInfo.NUMBERED_LIST;
import static kr.co.nogibackend.domain.notion.info.NotionBlockInfo.TABLE;
import static kr.co.nogibackend.domain.notion.info.NotionBlockInfo.TOGGLE;
import static kr.co.nogibackend.response.code.NotionResponseCode.F_PREPROCESS_MARKDOWN;

import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.notion.content.NotionListItemContent;
import kr.co.nogibackend.domain.notion.content.NotionTableRowContent;
import kr.co.nogibackend.domain.notion.feignclient.NotionClient;
import kr.co.nogibackend.domain.notion.info.NotionBaseInfo;
import kr.co.nogibackend.domain.notion.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.info.NotionPageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotionDataProvider {

  private final NotionClient notionClient;

  public void preprocessMarkdown(
      NotionBaseInfo<NotionBlockInfo> blocks
      , String notionAccessToken
  ) {
    try {
      for (NotionBlockInfo block : blocks.getResults()) {
        switch (block.getType()) {
          case TABLE:
            List<NotionTableRowContent> tableRows =
                this.getBlocks(notionAccessToken, block.getId())
                    .getResults()
                    .stream()
                    .map(NotionBlockInfo::getTable_row)
                    .toList();
            block.getTable().setRows(tableRows);
            break;

          case IMAGE:
            URI imageUri = block.getImage().createURL();
            String base64 = this.getImageOfBlock(imageUri);
            block.getImage().setBase64(base64);
            break;

          case BULLETED_LIST:
            this.getChildrenOfListItem(notionAccessToken, block, false);
            break;

          case NUMBERED_LIST:
            this.getChildrenOfListItem(notionAccessToken, block, true);
            break;

          case TOGGLE:
            this.getChildrenOfToggle(notionAccessToken, block);
            break;
        }
      }
    } catch (Exception error) {
      log.error("NotionDataProvider PreprocessMarkdown Error: {}", error.getMessage());
      throw new GlobalException(F_PREPROCESS_MARKDOWN);
    }
  }

  /**
   * <h1>Toggle 자식요소 조회</h1>
   */
  private void getChildrenOfToggle(
      String accessToken
      , NotionBlockInfo parentBlock
  ) {
    if (!parentBlock.isHas_children()) {
      return;
    }

    // 자식 블럭 조회
    NotionBaseInfo<NotionBlockInfo> childrenBlock =
        this.getBlocks(accessToken, parentBlock.getId());

    // 자식 블럭들 마크다운 전처리
    this.preprocessMarkdown(childrenBlock, accessToken);

    // 전처리 된 자식블럭들 toggle children에 추가
    parentBlock.getToggle().setChildren(childrenBlock.getResults());
  }

  /**
   * <h1>List Item의 자식요소 조회</h1>
   */
  private void getChildrenOfListItem(
      String accessToken
      , NotionBlockInfo parentBlock
      , boolean isNumberedList
  ) {
    if (!parentBlock.isHas_children()) {
      return;
    }

    // 자식 블럭 조회
    NotionBaseInfo<NotionBlockInfo> childrenBlock =
        this.getBlocks(accessToken, parentBlock.getId());

    // 자식 블럭에서 ListItemContent 추출
    List<NotionListItemContent> childrenList =
        childrenBlock
            .getResults()
            .stream()
            .map((result) ->
                isNumberedList
                    ? result.getNumbered_list_item()
                    : result.getBulleted_list_item())
            .toList();

    // 부모 블럭에 자식 블럭을 추가
    if (isNumberedList) {
      parentBlock.getNumbered_list_item().setChildren(childrenList);
    } else {
      parentBlock.getBulleted_list_item().setChildren(childrenList);
    }

    // 자식 블럭(자식 블럭은 list)에 또 하위 자식 블럭 조회 후 추가
    for (NotionBlockInfo block : childrenBlock.getResults()) {
      this.getChildrenOfListItem(accessToken, block, isNumberedList);
    }
  }

  private String getImageOfBlock(URI uri) {
    byte[] imageByte = notionClient.getBlockImage(uri);
    return Base64.getEncoder().encodeToString(imageByte);
  }

  // 노션 페이지의 블럭을 모두 불러오기(1회 최대 100개만 가져올 수 있음)
  public NotionBaseInfo<NotionBlockInfo> getBlocks(
      String notionBotToken
      , String parentBlockId
  ) {
    NotionBaseInfo<NotionBlockInfo> blocks =
        notionClient.getBlocksFromParent(notionBotToken, parentBlockId);

    // hasMore 이 true 면 next_cursor 로 다음 블럭을 가져온다.
    boolean hasMore = blocks.isHas_more();
    String nextCursor = blocks.getNext_cursor();

    while (hasMore) {
      NotionBaseInfo<NotionBlockInfo> nextBlocks =
          notionClient.getBlocksFromParent(notionBotToken, parentBlockId, nextCursor);
      blocks.getResults().addAll(nextBlocks.getResults());
      hasMore = nextBlocks.isHas_more();
      nextCursor = nextBlocks.getNext_cursor();
    }

    return blocks;
  }

  public List<NotionPageInfo> getCompletedPages(String AuthToken, String databaseId) {
    try {
      Map<String, Object> filter = NotionRequestMaker.createPageStatusEqFilter(STATUS_COMPLETED);
      return
          notionClient
              .getPagesFromDatabase(AuthToken, databaseId, filter)
              .getResults();
    } catch (Exception error) {
      return List.of();
    }
  }

  public Optional<NotionBlockInfo> getNotionDatabaseByPageId(
      String notionAccessToken
      , String pageId
  ) {
    return
        notionClient
            .getBlocksFromParent(notionAccessToken, pageId)
            .getResults()
            .stream()
            // todo: child_database 상수로 빼기
            .filter(blocks -> "child_database".equals(blocks.getType()))
            .findFirst();
  }

}
