package kr.co.nogibackend.interfaces.guide;

import kr.co.nogibackend.config.security.Auth;
import kr.co.nogibackend.domain.guide.GuideService;
import kr.co.nogibackend.domain.guide.dto.request.GuideRegisterRequest;
import kr.co.nogibackend.domain.guide.dto.request.GuideUpdateRequest;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guides")
@RequiredArgsConstructor
public class GuideCommandController {

  private final GuideService guideService;

  // todo: userId 등록 필요, 지금 하드코딩 되어잇음
  @PostMapping
  public ResponseEntity<?> registerGuide(
      @Validated @RequestBody GuideRegisterRequest request
      , Auth auth
  ) {
    return Response.success(guideService.registerGuide(request.toCommand()));
  }

  @PutMapping
  public ResponseEntity<?> updateGuide(@Validated @RequestBody GuideUpdateRequest request) {
    return Response.success(guideService.updateGuide(request.toCommand()));
  }

  @DeleteMapping
  public ResponseEntity<?> deleteGuide(@RequestParam("id") Long guideId) {
    return Response.success(guideService.deleteGuide(guideId));
  }

}
