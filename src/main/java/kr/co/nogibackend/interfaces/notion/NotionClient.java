package kr.co.nogibackend.interfaces.notion;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import kr.co.nogibackend.config.openfeign.NotionFeignClientConfig;
import kr.co.nogibackend.interfaces.notion.response.DemoResponse;
import kr.co.nogibackend.interfaces.notion.response.NotionBlockResponse;
import kr.co.nogibackend.interfaces.notion.response.NotionPageResponse;
import kr.co.nogibackend.interfaces.notion.response.NotionResponse;

/*
  Package Name : kr.co.nogibackend.interfaces.notion
  File Name    : NotionOutController
  Author       : superpil
  Created Date : 25. 1. 26.
  Description  :
 */
@FeignClient(name = "NotionClient", url = "https://api.notion.com/v1", configuration = NotionFeignClientConfig.class)
public interface NotionClient {

	@GetMapping("/posts")
	ResponseEntity<List<DemoResponse>> getDemo();

	@PostMapping("/databases/{databaseId}/query")
	ResponseEntity<NotionResponse<NotionPageResponse>> getPagesFromDatabase(
		@RequestHeader("Authorization") String authToken,
		@PathVariable(value = "databaseId") String databaseId,
		Map<String, Object> request
	);

	@GetMapping("/blocks/{pageId}/children?page_size=100")
	ResponseEntity<NotionResponse<NotionBlockResponse>> getBlocksFromPage(
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
