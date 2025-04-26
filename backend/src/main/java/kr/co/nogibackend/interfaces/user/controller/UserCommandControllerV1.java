package kr.co.nogibackend.interfaces.user.controller;

import kr.co.nogibackend.application.user.UserUpdateFacade;
import kr.co.nogibackend.global.config.security.Auth;
import kr.co.nogibackend.global.response.service.Response;
import kr.co.nogibackend.interfaces.user.request.UserUpdateRequest;
import kr.co.nogibackend.interfaces.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserCommandControllerV1 {

	private final UserUpdateFacade userUpdateFacade;

	@PatchMapping("/me")
	public ResponseEntity<?> updateMeAndCreateRepo(
			@RequestBody UserUpdateRequest request,
			Auth auth
	) {
		return Response.success(
				UserResponse.from(
						userUpdateFacade.updateUserAndCreateRepo(request.toCommand(auth.getUserId()))
				)
		);
	}

}
