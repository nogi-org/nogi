package kr.co.nogibackend.interfaces.notice;

import kr.co.nogibackend.domain.notice.service.NoticeGetService;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeGetController {

  private final NoticeGetService noticeGetService;

  @GetMapping("/page")
  public ResponseEntity<?> publish(Pageable pageable) {
    return Response.success(noticeGetService.getPage(pageable));
  }

}
