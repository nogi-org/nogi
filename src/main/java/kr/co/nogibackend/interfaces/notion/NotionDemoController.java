package kr.co.nogibackend.interfaces.notion;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.nogibackend.domain.notion.NotionService;
import kr.co.nogibackend.domain.notion.dto.command.NotionStartTILCommand;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/samples")
@RequiredArgsConstructor
public class NotionDemoController {

	private final NotionService notionService;

	@GetMapping("/demo/notion")
	public ResponseEntity<?> getNotion(
		@RequestParam("AuthToken") String AuthToken,
		@RequestParam("databaseId") String databaseId
	) {
		User user = User.builder().notionBotToken(AuthToken).notionDatabaseId(databaseId).build();
		return Response.success(
			notionService.startTIL(NotionStartTILCommand
				.builder()
				.notionBotToken(user.getNotionBotToken())
				.notionDatabaseId(user.getNotionDatabaseId())
				.build())
		);
	}

	@GetMapping("/demo/notion/update")
	public ResponseEntity<?> getNotionUpdate(
		@RequestParam("AuthToken") String AuthToken,
		@RequestParam("pageId") String pageId
	) {
		return Response.success(
			null
		);
	}

}
