package kr.co.nogibackend.infra.notion;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import kr.co.nogibackend.config.openfeign.NotionFeignClientConfig;
import kr.co.nogibackend.domain.notion.dto.info.DemoInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
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

	@GetMapping("/posts")
	ResponseEntity<List<DemoInfo>> getDemo();

	@PostMapping("/databases/{databaseId}/query")
	ResponseEntity<NotionInfo<NotionPageInfo>> getPagesFromDatabase(
		@RequestHeader("Authorization") String authToken,
		@PathVariable(value = "databaseId") String databaseId,
		Map<String, Object> request
	);

	@GetMapping("/blocks/{pageId}/children?page_size=100")
	ResponseEntity<NotionInfo<NotionBlockInfo>> getBlocksFromPage(
		@RequestHeader("Authorization") String authToken,
		@PathVariable(value = "pageId") String pageId
	);

	//  @PatchMapping("/pages/{pageId}")
	//  ResponseEntity<NotionDatabaseResponse> updatePageStatus(
	//      @RequestHeader("Authorization") String authToken,
	//      @PathVariable(value = "pageId") String pageId,
	//      @RequestBody Map<String, Object> request
	//  );

}
