package kr.co.nogibackend.interfaces.guide;

import kr.co.nogibackend.domain.guide.GuideService;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guides")
@RequiredArgsConstructor
public class GuideGetController {

  private final GuideService guideService;

  @GetMapping
  public ResponseEntity<?> getGuides() {
    return Response.success(guideService.getGuides());
  }

}
