package kr.co.nogibackend.interfaces.sample;

import static kr.co.nogibackend.response.code.CommonResponseCode.*;
import static kr.co.nogibackend.response.code.GitResponseCode.*;
import static kr.co.nogibackend.response.code.NotionResponseCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.nogibackend.application.sample.SampleFacade;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.sample.SampleService;
import kr.co.nogibackend.infra.notion.NotionFeignClient;
import kr.co.nogibackend.response.service.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/samples")
@RequiredArgsConstructor
public class SampleController {

	private final SampleService sampleService;
	private final SampleFacade sampleFacade;
	private final NotionFeignClient notionFeignClient;

	@GetMapping
	public ResponseEntity<List<SampleDto.Response>> findAll() {
		return ResponseEntity.ok().body(
			sampleService.findAll().stream().map(SampleDto.Response::from).toList()
		);
	}

	@GetMapping("{id}")
	public ResponseEntity<SampleDto.Response> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(
			SampleDto.Response.from(sampleService.findById(id))
		);
	}

	@PostMapping
	public ResponseEntity<Void> create(SampleDto.CreateRequest request) {
		sampleService.create(request.toCommand());
		return ResponseEntity.ok().build();
	}

	@PatchMapping("{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, SampleDto.UpdateRequest request) {
		sampleService.update(id, request.toCommand());
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		sampleService.delete(id);
		return ResponseEntity.ok().build();
	}

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
