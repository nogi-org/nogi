package kr.co.nogibackend.interfaces.github.controller;

import kr.co.nogibackend.application.user.UserValidateFacade;
import kr.co.nogibackend.global.config.security.Auth;
import kr.co.nogibackend.global.response.service.Response;
import kr.co.nogibackend.interfaces.github.response.UserValidateGithubResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/github")
@RequiredArgsConstructor
public class GithubValidationControllerV1 {

	private final UserValidateFacade userValidateFacade;

	@GetMapping("/validate")
	public ResponseEntity<?> validateGithub(Auth auth) {
		return Response.success(
				UserValidateGithubResponse.from(userValidateFacade.validateGithub(auth.getUserId()))
		);
	}

}
