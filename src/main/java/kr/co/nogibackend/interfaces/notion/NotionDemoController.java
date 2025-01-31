package kr.co.nogibackend.interfaces.notion;

import kr.co.nogibackend.domain.notion.NotionService;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/samples")
@RequiredArgsConstructor
public class NotionDemoController {

  private final NotionService notionService;

  @GetMapping("/demo/notion")
  public ResponseEntity<?> getNotion(
      @RequestParam("authToken") String authToken,
      @RequestParam("databaseId") String databaseId
  ) {
    return Response.success(notionService.demo(authToken, databaseId));
  }

}
