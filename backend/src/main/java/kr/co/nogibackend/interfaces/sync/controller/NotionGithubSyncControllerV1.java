package kr.co.nogibackend.interfaces.sync.controller;

import kr.co.nogibackend.application.sync.NotionGithubSyncFacade;
import kr.co.nogibackend.global.config.security.Auth;
import kr.co.nogibackend.global.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sync")
@RequiredArgsConstructor
public class NotionGithubSyncControllerV1 {

	private final NotionGithubSyncFacade notionGithubSyncFacade;

	@PostMapping("/manual")
	public ResponseEntity<?> onManualSync(Auth auth) {
		notionGithubSyncFacade.onManualSync(auth.getUserId());
		return Response.success();
	}

}
