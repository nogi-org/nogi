package kr.co.nogibackend.interfaces.notice.controller;

import kr.co.nogibackend.application.notice.NoticePublishFacade;
import kr.co.nogibackend.domain.notice.service.NoticePublishService;
import kr.co.nogibackend.domain.notice.service.NoticeReadService;
import kr.co.nogibackend.global.response.service.Response;
import kr.co.nogibackend.interfaces.notice.request.NoticePublishRequest;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeControllerV1 {

	private final NoticePublishFacade noticePublishFacade;
	private final NoticePublishService noticePublishService;
	private final NoticeReadService noticeReadService;

	@PostMapping
	public ResponseEntity<?> publish(@RequestBody NoticePublishRequest request) {
		return Response.success(noticePublishFacade.publish(request.toCommand()));
	}

	@PostMapping("/{noticeId}/re-publish")
	public ResponseEntity<?> rePublish(@PathVariable("noticeId") Long noticeId) {
		return Response.success(noticePublishService.rePublish(noticeId));
	}

	@GetMapping("/{noticeId}/recipients")
	public ResponseEntity<?> getRecipients(
			@PathVariable("noticeId") Long noticeId
			, NoticeRecipientsRequest request
			, Pageable pageable
	) {
		return Response.success(noticeReadService.getRecipients(noticeId, request, pageable));
	}

}
