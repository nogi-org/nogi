package kr.co.nogibackend.interfaces.notion.controller;

import kr.co.nogibackend.domain.notion.service.NotionConnectionService;
import kr.co.nogibackend.global.config.security.Auth;
import kr.co.nogibackend.global.response.service.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/notion")
@RequiredArgsConstructor
public class NotionConnectionControllerV1 {

	private final NotionConnectionService notionConnectionService;

	@GetMapping("/connection-check")
	public ResponseEntity<?> onConnectionTest(Auth auth) {
		return Response.success(notionConnectionService.onConnectionCheck(auth.getUserId()));
	}

}
