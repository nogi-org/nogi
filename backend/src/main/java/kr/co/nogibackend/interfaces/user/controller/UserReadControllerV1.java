package kr.co.nogibackend.interfaces.user.controller;

import kr.co.nogibackend.domain.user.service.UserService;
import kr.co.nogibackend.global.config.security.Auth;
import kr.co.nogibackend.global.response.service.Response;
import kr.co.nogibackend.interfaces.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserReadControllerV1 {

	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<?> getMe(Auth auth) {
		return Response.success(UserResponse.from(userService.findUserById(auth.getUserId())));
	}

}
