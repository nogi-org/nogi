package kr.co.nogibackend.infra.notion;

import static kr.co.nogibackend.response.code.NotionResponseCode.F_GET_BLOCK_IMAGE;
import static kr.co.nogibackend.response.code.NotionResponseCode.F_GET_NOTION_BLOCK;
import static kr.co.nogibackend.response.code.NotionResponseCode.F_GET_NOTION_DATABASE;
import static kr.co.nogibackend.response.code.NotionResponseCode.F_GET_NOTION_PAGE;
import static kr.co.nogibackend.response.code.NotionResponseCode.F_UPDATE_TIL_STATUS;

import java.net.URI;
import java.util.Map;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.admin.dto.request.NotionCreateNoticeRequest;
import kr.co.nogibackend.domain.notion.feignclient.NotionClient;
import kr.co.nogibackend.domain.notion.info.NotionBaseInfo;
import kr.co.nogibackend.domain.notion.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.info.NotionDatabaseInfo;
import kr.co.nogibackend.domain.notion.info.NotionGetAccessInfo;
import kr.co.nogibackend.domain.notion.info.NotionPageInfo;
import kr.co.nogibackend.interfaces.notion.dto.request.NotionGetAccessTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotionClientImpl implements NotionClient {

  private final NotionFeignClient notionFeignClient;
  private final NotionImageFeignClient notionImageFeignClient;

  @Override
  public NotionBaseInfo<NotionPageInfo> getPagesFromDatabase(
      String BotToken,
      String databaseId,
      Map<String, Object> request
  ) {
    try {
      return
          notionFeignClient
              .getPagesFromDatabase(BotToken, databaseId, request)
              .getBody();
    } catch (Exception error) {
      log.error("Notion Get Page From Database API Fail Message : {}", error.getMessage());
      throw new GlobalException(F_GET_NOTION_PAGE);
    }
  }

  @Override
  public NotionBaseInfo<NotionBlockInfo> getBlocksFromParent(
      String accessToken
      , String parentBlockId
      , String startCursor
  ) {
    return this.callBlocksFromParentClient(accessToken, parentBlockId, startCursor);
  }

  @Override
  public NotionBaseInfo<NotionBlockInfo> getBlocksFromParent(
      String accessToken,
      String parentBlockId
  ) {
    return this.callBlocksFromParentClient(accessToken, parentBlockId, null);
  }

  private NotionBaseInfo<NotionBlockInfo> callBlocksFromParentClient(
      String accessToken
      , String parentBlockId
      , String startCursor
  ) {
    try {
      return
          notionFeignClient
              .getBlocksFromParent(accessToken, parentBlockId, startCursor)
              .getBody();
    } catch (Exception error) {
      log.error("Notion Get Blocks API Fail Message: {}", error.getMessage());
      throw new GlobalException(F_GET_NOTION_BLOCK);
    }
  }

  @Override
  public byte[] getBlockImage(URI baseUri) {
    try {
      return notionImageFeignClient.getBlockImage(baseUri);
    } catch (Exception error) {
      throw new GlobalException(F_GET_BLOCK_IMAGE);
    }
  }

  @Override
  public NotionPageInfo updatePageStatus(
      String BotToken
      , String pageId
      , Map<String, Object> request
  ) {
    try {
      return
          notionFeignClient
              .updatePageStatus(BotToken, pageId, request)
              .getBody();
    } catch (Exception error) {
      throw new GlobalException(F_UPDATE_TIL_STATUS);
    }

  }

  @Override
  public NotionDatabaseInfo getDatabase(
      String BotToken
      , String databaseId
  ) {
    try {
      return
          notionFeignClient.getDatabase(BotToken, databaseId).getBody();
    } catch (Exception error) {
      throw new GlobalException(F_GET_NOTION_DATABASE);
    }
  }

  @Override
  public NotionGetAccessInfo getAccessToken(
      String basicToken,
      NotionGetAccessTokenRequest request
  ) {
    return notionFeignClient.getAccessToken(basicToken, request).getBody();
  }

  @Override
  public NotionBaseInfo<NotionPageInfo> createPage(
      String basicToken,
      NotionCreateNoticeRequest request
  ) {
    return notionFeignClient.createPage(basicToken, request).getBody();
  }

}
