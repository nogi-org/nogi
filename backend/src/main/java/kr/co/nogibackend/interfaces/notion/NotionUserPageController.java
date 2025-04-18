package kr.co.nogibackend.interfaces.notion;

import kr.co.nogibackend.domain.notion.service.NotionUserPageService;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notion")
@RequiredArgsConstructor
public class NotionUserPageController {

  private final NotionUserPageService notionUserPageService;

  // todo: security에 어드민만 가능하게
  @GetMapping("/{userId}/pages")
  public ResponseEntity<?> getPages(
      @PathVariable("userId") Long userId
      , String nextCursor
  ) {
    return Response.success(notionUserPageService.getPages(userId, nextCursor));
  }

  // todo: security에 어드민만 가능하게
  @GetMapping("/{userId}/{pageId}/page")
  public ResponseEntity<?> getPage(
      @PathVariable("userId") Long userId,
      @PathVariable("pageId") String pageId
  ) {
    return Response.success(notionUserPageService.getPage(userId, pageId));
  }

}
