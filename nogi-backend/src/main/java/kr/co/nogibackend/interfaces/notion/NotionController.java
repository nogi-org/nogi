package kr.co.nogibackend.interfaces.notion;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.nogibackend.domain.notion.NotionService;
import kr.co.nogibackend.interfaces.notion.dto.NotionConnectionTestRequest;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
  Package Name : kr.co.nogibackend.interfaces.user
  File Name    : LoginController
  Author       : won taek oh
  Created Date : 25. 2. 14.
  Description  :
 */
@Slf4j
@RequestMapping("/notion")
@RequiredArgsConstructor
public class NotionController {

	private final NotionService notionService;

	@GetMapping("connection-test")
	public ResponseEntity<?> onConnectionTest(
		@Validated @RequestBody NotionConnectionTestRequest request
	) {
		return Response.success(notionService.onConnectionTest(request.toCommand()));
	}

}
