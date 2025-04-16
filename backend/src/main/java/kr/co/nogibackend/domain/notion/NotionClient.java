package kr.co.nogibackend.domain.notion;

import java.net.URI;
import java.util.Map;
import kr.co.nogibackend.domain.admin.dto.request.NotionCreateNoticeRequest;
import kr.co.nogibackend.domain.notion.dto.info.NotionBaseInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionDatabaseInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionGetAccessInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import kr.co.nogibackend.interfaces.notion.dto.request.NotionGetAccessTokenRequest;

public interface NotionClient {

  NotionBaseInfo<NotionPageInfo> getPagesFromDatabase
      (String BotToken, String databaseId, Map<String, Object> request);

  NotionBaseInfo<NotionBlockInfo> getBlocksFromParent
      (String BotToken, String pageId, String startCursor);

  NotionBaseInfo<NotionBlockInfo> getBlocksFromParent
      (String BotToken, String pageId);

  byte[] getBlockImage(URI baseUri);

  NotionPageInfo updatePageStatus
      (String BotToken, String pageId, Map<String, Object> request);

  NotionDatabaseInfo getDatabase(String BotToken, String databaseId);

  NotionGetAccessInfo getAccessToken(String basicToken, NotionGetAccessTokenRequest request);

  NotionBaseInfo<NotionPageInfo> createPage(String basicToken, NotionCreateNoticeRequest request);

}
