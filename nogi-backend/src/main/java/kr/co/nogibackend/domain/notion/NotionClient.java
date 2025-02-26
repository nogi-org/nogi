package kr.co.nogibackend.domain.notion;

import java.net.URI;
import java.util.Map;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;

public interface NotionClient {

  NotionInfo<NotionPageInfo> getPagesFromDatabase(String AuthToken, String databaseId,
      Map<String, Object> request);

  NotionInfo<NotionBlockInfo> getBlocksFromPage(String AuthToken, String pageId,
      String startCursor);

  byte[] getBlockImage(URI baseUri);

  NotionPageInfo updatePageStatus
      (String AuthToken, String pageId, Map<String, Object> request);

}
