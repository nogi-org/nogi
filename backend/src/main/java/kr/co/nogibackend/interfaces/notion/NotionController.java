package kr.co.nogibackend.interfaces.notion;

import kr.co.nogibackend.config.security.Auth;
import kr.co.nogibackend.domain.notion.service.NotionService;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notion")
@RequiredArgsConstructor
public class NotionController {

	private final NotionService notionService;

	@PostMapping("connection-test")
	public ResponseEntity<?> onConnectionTest(Auth auth) {
		return Response.success(notionService.onConnectionTest(auth.getUserId()));
	}

  /*
  todo: 노션페이지에 공지사항 남기기
  노션 연결 테스트를 활용해서 노션 페이지ID를 가져와야함. 노션은 연결 되어있지만 페이지ID가 다를 수 잇음
   */

}
