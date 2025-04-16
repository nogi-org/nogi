package kr.co.nogibackend.interfaces.user;

import kr.co.nogibackend.application.user.UserValidateFacade;
import kr.co.nogibackend.config.security.Auth;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.interfaces.user.dto.UserResponse;
import kr.co.nogibackend.interfaces.user.dto.UserValidateGithubResponse;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserGetController {

  private final UserService userService;
  private final UserValidateFacade userValidateFacade;

  @GetMapping
  public ResponseEntity<?> getUser(Auth auth) {
    return Response.success(UserResponse.from(userService.findUserById(auth.getUserId())));
  }

  @GetMapping("github/validate")
  public ResponseEntity<?> validateGithub(Auth auth) {
    return Response.success(
        UserValidateGithubResponse.from(userValidateFacade.validateGithub(auth.getUserId()))
    );
  }
}
