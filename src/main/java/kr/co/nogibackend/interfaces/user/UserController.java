package kr.co.nogibackend.interfaces.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.nogibackend.application.user.UserFacade;
import kr.co.nogibackend.config.security.Auth;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.interfaces.user.dto.UserResponse;
import kr.co.nogibackend.interfaces.user.dto.UserUpdateRequest;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;

/*
  Package Name : kr.co.nogibackend.interfaces.user
  File Name    : UserController
  Author       : won taek oh
  Created Date : 25. 2. 14.
  Description  :
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserFacade userFacade;

	@GetMapping
	public ResponseEntity<?> getUser(Auth auth) {
		return Response.success(UserResponse.from(userService.findUserById(auth.getUserId())));
	}

	@PatchMapping("{id}")
	public ResponseEntity<?> updateUser(
		@PathVariable Long id,
		@RequestBody UserUpdateRequest request
	) {
		return Response.success(
			UserResponse.from(
				userFacade.updateUserAndCreateRepo(request.toCommand(id))
			)
		);
	}

}
