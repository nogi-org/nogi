package kr.co.nogibackend.domain.notion;

import java.net.URI;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;

public interface NotionClient {

	NotionInfo<NotionPageInfo> getPagesFromDatabase(String authToken, String databaseId, Map<String, Object> request);

	NotionInfo<NotionBlockInfo> getBlocksFromPage(String authToken, String pageId, String startCursor);

	byte[] getBlockImage(URI baseUri);

	ResponseEntity<NotionPageInfo> updatePageStatus
		(String authToken, String pageId, Map<String, Object> request);

}
