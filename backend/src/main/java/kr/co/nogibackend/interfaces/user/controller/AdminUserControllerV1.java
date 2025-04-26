package kr.co.nogibackend.interfaces.user.controller;

import kr.co.nogibackend.domain.user.service.UserService;
import kr.co.nogibackend.global.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserControllerV1 {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<?> getUsers() {
		return Response.success(userService.getUsers());
	}

}
