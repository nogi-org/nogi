package kr.co.nogibackend.interfaces.user;

import kr.co.nogibackend.application.nogi.NogiFacade;
import kr.co.nogibackend.application.user.UserFacade;
import kr.co.nogibackend.application.user.dto.UserFacadeCommand;
import kr.co.nogibackend.config.security.Auth;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.interfaces.user.dto.UserResponse;
import kr.co.nogibackend.interfaces.user.dto.UserUpdateRequest;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  private final NogiFacade nogiFacade;

  @GetMapping
  public ResponseEntity<?> getUser(Auth auth) {
    return Response.success(UserResponse.from(userService.findUserById(auth.getUserId())));
  }

  @PatchMapping
  public ResponseEntity<?> updateUser(
      @RequestBody UserUpdateRequest request,
      Auth auth
  ) {
    return Response.success(
        UserResponse.from(
            userFacade.updateUserAndCreateRepo(request.toCommand(auth.getUserId()))
        )
    );
  }

  @GetMapping("validate-repository-name")
  public ResponseEntity<?> updateUser(
      @RequestParam String repositoryName,
      Auth auth
  ) {
    userFacade.validateRepositoryName(
        new UserFacadeCommand.ValidateRepositoryName(auth.getUserId(), repositoryName)
    );
    return Response.success();
  }

  @PostMapping("manual-nogi")
  public ResponseEntity<?> onManualNogi(Auth auth) {
    nogiFacade.onManual(auth.getUserId());
    return Response.success();
  }

}
