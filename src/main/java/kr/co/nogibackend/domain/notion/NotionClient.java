package kr.co.nogibackend.domain.notion;

import java.net.URI;
import java.util.Map;

import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;

public interface NotionClient {

	NotionInfo<NotionPageInfo> getPagesFromDatabase(String authToken, String databaseId, Map<String, Object> request);

	NotionInfo<NotionBlockInfo> getBlocksFromPage(String authToken, String pageId, String startCursor);

	byte[] getBlockImage(URI baseUri, String path);

}
