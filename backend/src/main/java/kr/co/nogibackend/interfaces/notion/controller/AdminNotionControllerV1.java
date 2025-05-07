package kr.co.nogibackend.interfaces.notion.controller;

import static kr.co.nogibackend.global.response.code.NotionResponseCode.F_PROPERTY_PARSING;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kr.co.nogibackend.domain.notion.service.NotionDatabaseService;
import kr.co.nogibackend.domain.notion.service.NotionPageService;
import kr.co.nogibackend.global.response.service.Response;
import kr.co.nogibackend.interfaces.notion.response.NotionPageIdUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin/notion")
@RequiredArgsConstructor
public class AdminNotionControllerV1 {

	private final NotionPageService notionPageService;
	private final NotionDatabaseService notionDatabaseService;
	private final ObjectMapper objectMapper;

	@PutMapping("/users/page-ids")
	public ResponseEntity<?> updateNotionPageId() {
		return Response.success(
				NotionPageIdUpdateResponse.from(notionPageService.updateNotionPageId()));
	}

	@GetMapping("/users/{userId}/pages")
	public ResponseEntity<?> getPages(
			@PathVariable("userId") Long userId
			, String nextCursor
	) {
		return Response.success(notionPageService.getPages(userId, nextCursor));
	}

	@GetMapping("/users/{userId}/pages/{pageId}")
	public ResponseEntity<?> getPage(
			@PathVariable("userId") Long userId,
			@PathVariable("pageId") String pageId
	) {
		return Response.success(notionPageService.getPage(userId, pageId));
	}

	@GetMapping("/users/{userId}/database")
	public ResponseEntity<?> getDatabase(
			@PathVariable("userId") Long userId
	) {
		return Response.success(notionDatabaseService.getDatabase(userId));
	}

	@PostMapping("/database/property")
	public ResponseEntity<?> createDatabaseProperty(@RequestBody String property) {
		try {
			Map<String, Object> parsing =
					objectMapper.readValue(property, new TypeReference<Map<String, Object>>() {
					});
			return Response.success(notionDatabaseService.createProperty(parsing));
		} catch (JsonProcessingException e) {
			return Response.fail(F_PROPERTY_PARSING);
		}
	}

}
