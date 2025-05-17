package kr.co.nogibackend.interfaces.notice.controller;

import kr.co.nogibackend.domain.notice.service.NoticeReadService;
import kr.co.nogibackend.global.response.service.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/notices")
@RequiredArgsConstructor
public class NoticeReadControllerV1 {

	private final NoticeReadService noticeReadService;

	@GetMapping
	public ResponseEntity<?> getNotices(Pageable pageable) {
		return Response.success(noticeReadService.getNotices(pageable));
	}

	@GetMapping("/{noticeId}")
	public ResponseEntity<?> getNotice(@PathVariable("noticeId") Long noticeId) {
		return Response.success(noticeReadService.getNotice(noticeId));
	}

}
