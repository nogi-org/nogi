package kr.co.nogibackend.domain.notion;

import java.net.URI;
import java.util.Map;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionDatabaseInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;

public interface NotionClient {

  NotionInfo<NotionPageInfo> getPagesFromDatabase(String BotToken, String databaseId,
      Map<String, Object> request);

  NotionInfo<NotionBlockInfo> getBlocksFromPage(String BotToken, String pageId, String startCursor);

  byte[] getBlockImage(URI baseUri);

  NotionPageInfo updatePageStatus
      (String BotToken, String pageId, Map<String, Object> request);

  NotionDatabaseInfo getDatabase(String BotToken, String databaseId);

}
