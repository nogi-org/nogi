package kr.co.nogibackend.interfaces.user;

import kr.co.nogibackend.application.nogi.NogiFacade;
import kr.co.nogibackend.application.user.UserUpdateFacade;
import kr.co.nogibackend.config.security.Auth;
import kr.co.nogibackend.interfaces.user.dto.UserResponse;
import kr.co.nogibackend.interfaces.user.dto.UserUpdateRequest;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCommandController {

  private final UserUpdateFacade userUpdateFacade;
  private final NogiFacade nogiFacade;

  @PatchMapping
  public ResponseEntity<?> updateUser(
      @RequestBody UserUpdateRequest request,
      Auth auth
  ) {
    return Response.success(
        UserResponse.from(
            userUpdateFacade.updateUserAndCreateRepo(request.toCommand(auth.getUserId()))
        )
    );
  }

  @PostMapping("manual-nogi")
  public ResponseEntity<?> onManualNogi(Auth auth) {
    nogiFacade.onManual(auth.getUserId());
    return Response.success();
  }

}
