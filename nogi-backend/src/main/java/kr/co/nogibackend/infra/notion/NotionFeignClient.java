package kr.co.nogibackend.infra.notion;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.nogibackend.config.openfeign.NotionFeignClientConfig;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionDatabaseInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;

/*
  Package Name : kr.co.nogibackend.infra.notion
  File Name    : NotionFeignClient
  Author       : superpil
  Created Date : 25. 1. 26.
  Description  :
 */
@FeignClient(name = "NotionClient", url = "https://api.notion.com/v1", configuration = NotionFeignClientConfig.class)
public interface NotionFeignClient {

	@PostMapping("/databases/{databaseId}/query")
	ResponseEntity<NotionInfo<NotionPageInfo>> getPagesFromDatabase(
		@RequestHeader("Authorization") String AuthToken,
		@PathVariable(value = "databaseId") String databaseId,
		Map<String, Object> request
	);

	@GetMapping("/blocks/{pageId}/children?page_size=100")
	ResponseEntity<NotionInfo<NotionBlockInfo>> getBlocksFromPage(
		@RequestHeader("Authorization") String AuthToken,
		@PathVariable(value = "pageId") String pageId,
		@RequestParam(value = "start_cursor", required = false) String startCursor
	);

	@RequestMapping(method = RequestMethod.PATCH, value = "/pages/{pageId}")
	ResponseEntity<NotionPageInfo> updatePageStatus(
		@RequestHeader("Authorization") String AuthToken,
		@PathVariable(value = "pageId") String pageId,
		@RequestBody Map<String, Object> request
	);


	@RequestMapping(method = RequestMethod.GET, value = "/databases/{databaseId}")
	ResponseEntity<NotionDatabaseInfo> getDatabase(
		@RequestHeader("Authorization") String AuthToken,
		@PathVariable(value = "databaseId") String databaseId
	);

}
