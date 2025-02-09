package kr.co.nogibackend.interfaces.sample;

import static kr.co.nogibackend.response.code.CommonResponseCode.*;
import static kr.co.nogibackend.response.code.GitResponseCode.*;
import static kr.co.nogibackend.response.code.NotionResponseCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/samples")
@RequiredArgsConstructor
public class SampleController {

	@GetMapping("/demo")
	public ResponseEntity<?> findDemo() {
		int i = 6;

		if (i == 1) {
			// 성공 응답
			return Response.success();
		} else if (i == 2) {
			// 성공 응답 with 값
			List<String> list = new ArrayList<>();
			list.add("test1");
			list.add("test2");
			return Response.success(list);
		} else if (i == 3) {
			// 실패 응답
			return Response.fail(F_NULL_POINT);
		} else if (i == 4) {
			// 실패 응답 with 값
			List<String> list = new ArrayList<>();
			list.add("test1");
			list.add("test2");
			return Response.fail(F_NULL_POINT, list);
		} else if (i == 5) {
			// 공통 exception
			throw new GlobalException(F_FILE_URL_PARSING);
		} else {
			// 공통 exception with 값
			List<String> list = new ArrayList<>();
			list.add("test1");
			list.add("test2");
			throw new GlobalException(F_SAMPLE_GIT, list);
		}
	}

}
