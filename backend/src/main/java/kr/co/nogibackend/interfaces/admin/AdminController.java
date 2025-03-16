package kr.co.nogibackend.interfaces.admin;

import kr.co.nogibackend.domain.admin.AdminService;
import kr.co.nogibackend.domain.admin.dto.response.NotionPageIdUpdateResponse;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@PutMapping("/notion-page-id")
	public ResponseEntity<?> updateNotionPageId() {
		return Response.success(NotionPageIdUpdateResponse.from(adminService.updateNotionPageId()));
	}


}
