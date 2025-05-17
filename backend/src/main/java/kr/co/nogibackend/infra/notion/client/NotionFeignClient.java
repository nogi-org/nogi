package kr.co.nogibackend.infra.notion.client;

import java.util.Map;
import kr.co.nogibackend.domain.notion.command.NotionCreateNoticeCommand;
import kr.co.nogibackend.domain.notion.result.NotionBaseResult;
import kr.co.nogibackend.domain.notion.result.NotionBlockResult;
import kr.co.nogibackend.domain.notion.result.NotionDatabaseResult;
import kr.co.nogibackend.domain.notion.result.NotionGetAccessResult;
import kr.co.nogibackend.domain.notion.result.NotionPageResult;
import kr.co.nogibackend.global.config.openfeign.NotionFeignClientConfig;
import kr.co.nogibackend.interfaces.notion.request.NotionGetAccessTokenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NotionClient", url = "https://api.notion.com/v1", configuration = NotionFeignClientConfig.class)
public interface NotionFeignClient {

	@RequestMapping(method = RequestMethod.POST, value = "/databases/{databaseId}/query")
	ResponseEntity<NotionBaseResult<NotionPageResult>> getPagesFromDatabase(
			@RequestHeader("Authorization") String token,
			@PathVariable(value = "databaseId") String databaseId,
			Map<String, Object> request
	);

	@RequestMapping(method = RequestMethod.GET, value = "/blocks/{parentBlockId}/children?page_size=100")
	ResponseEntity<NotionBaseResult<NotionBlockResult>> getBlocksFromParent(
			@RequestHeader("Authorization") String token,
			@PathVariable(value = "parentBlockId") String parentBlockId,
			@RequestParam(value = "start_cursor", required = false) String startCursor
	);

	@RequestMapping(method = RequestMethod.PATCH, value = "/pages/{pageId}")
	ResponseEntity<NotionPageResult> updatePageStatus(
			@RequestHeader("Authorization") String token,
			@PathVariable(value = "pageId") String pageId,
			@RequestBody Map<String, Object> request
	);

	@RequestMapping(method = RequestMethod.GET, value = "/databases/{databaseId}")
	ResponseEntity<NotionDatabaseResult> getDatabase(
			@RequestHeader("Authorization") String token,
			@PathVariable(value = "databaseId") String databaseId
	);

	@RequestMapping(method = RequestMethod.POST, value = "/oauth/token")
	ResponseEntity<NotionGetAccessResult> getAccessToken(
			@RequestHeader("Authorization") String token,
			@RequestBody NotionGetAccessTokenRequest request
	);

	@RequestMapping(method = RequestMethod.POST, value = "/pages")
	ResponseEntity<NotionBaseResult<NotionPageResult>> createPage(
			@RequestHeader("Authorization") String token,
			@RequestBody NotionCreateNoticeCommand request
	);

	@RequestMapping(method = RequestMethod.PATCH, value = "/databases/{databaseId}")
	ResponseEntity<NotionDatabaseResult> patchDatabase(
			@RequestHeader("Authorization") String token,
			@PathVariable(value = "databaseId") String databaseId,
			@RequestBody Map<String, Object> request
	);

}
