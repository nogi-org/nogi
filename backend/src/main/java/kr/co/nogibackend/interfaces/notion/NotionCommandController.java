package kr.co.nogibackend.interfaces.notion;

import kr.co.nogibackend.config.security.Auth;
import kr.co.nogibackend.domain.notion.service.NotionConnectionService;
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
public class NotionCommandController {

  private final NotionConnectionService notionConnectionService;

  @PostMapping("connection-test")
  public ResponseEntity<?> onConnectionTest(Auth auth) {
    return Response.success(notionConnectionService.onConnectionTest(auth.getUserId()));
  }

}
