package kr.co.nogibackend.interfaces.notion;

import kr.co.nogibackend.domain.notion.NotionService;
import kr.co.nogibackend.interfaces.notion.dto.NotionConnectionTestRequest;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notion")
@RequiredArgsConstructor
public class NotionController {

  private final NotionService notionService;

  @PostMapping("connection-test")
  public ResponseEntity<?> onConnectionTest(
      @Validated @RequestBody NotionConnectionTestRequest request
  ) {
    notionService.onConnectionTest(request.toCommand());
    return Response.success();
  }

}
