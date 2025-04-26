package kr.co.nogibackend.interfaces.guide.controller;

import kr.co.nogibackend.domain.guide.service.GuideService;
import kr.co.nogibackend.global.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/guides")
@RequiredArgsConstructor
public class GuideReadControllerV1 {

	private final GuideService guideService;

	@GetMapping
	public ResponseEntity<?> getGuides() {
		return Response.success(guideService.getGuides());
	}

}
