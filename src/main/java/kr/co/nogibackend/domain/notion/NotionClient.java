package kr.co.nogibackend.domain.notion;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import kr.co.nogibackend.domain.notion.dto.info.DemoInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;

public interface NotionClient {
	ResponseEntity<List<DemoInfo>> getDemo();

	ResponseEntity<NotionInfo<NotionPageInfo>> getPagesFromDatabase(
		@RequestHeader("Authorization") String authToken,
		@PathVariable(value = "databaseId") String databaseId,
		Map<String, Object> request
	);

	ResponseEntity<NotionInfo<NotionBlockInfo>> getBlocksFromPage(
		@RequestHeader("Authorization") String authToken,
		@PathVariable(value = "pageId") String pageId
	);
}
