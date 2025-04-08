package kr.co.nogibackend.interfaces.notice;

import kr.co.nogibackend.application.notice.NoticePublishFacade;
import kr.co.nogibackend.interfaces.notice.request.NoticePublishRequest;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticePublishController {

  private final NoticePublishFacade noticePublishFacade;

  @PostMapping("publish")
  public ResponseEntity<?> publish(NoticePublishRequest request) {
    return Response.success(noticePublishFacade.publish(request.toCommand()));
  }

  // todo: 작업필요
//  @PostMapping("re-publish")
//  public ResponseEntity<?> rePublish(NoticePublishRequest request) {
//    return Response.success(noticePublishService.publish(request.toCommand()));
//  }

}
