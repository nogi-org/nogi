package kr.co.nogibackend.interfaces.notice;

import kr.co.nogibackend.domain.notice.service.NoticeGetService;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeGetController {

  private final NoticeGetService noticeGetService;

  @GetMapping("/notices")
  public ResponseEntity<?> getNotices(Pageable pageable) {
    return Response.success(noticeGetService.getNotices(pageable));
  }

  @GetMapping("/notice/{noticeId}")
  public ResponseEntity<?> getNotice(@PathVariable("noticeId") Long noticeId) {
    return Response.success(noticeGetService.getNotice(noticeId));
  }

  @GetMapping("/notice/{noticeId}/recipients")
  public ResponseEntity<?> getRecipients(
      @PathVariable("noticeId") Long noticeId
      , NoticeRecipientsRequest request
      , Pageable pageable
  ) {
    return Response.success(noticeGetService.getRecipients(noticeId, request, pageable));
  }

}
