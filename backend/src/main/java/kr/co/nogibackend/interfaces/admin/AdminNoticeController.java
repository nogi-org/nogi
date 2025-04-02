package kr.co.nogibackend.interfaces.admin;

import kr.co.nogibackend.domain.notice.service.NoticeCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminNoticeController {

	private final NoticeCreateService noticeCreateService;

	@PostMapping("/notice")
	public ResponseEntity<?> createNotice() {
		// return Response.success(NotionPageIdUpdateResponse.from(adminNoticeService.createNotice()));
		return null;
	}

}
