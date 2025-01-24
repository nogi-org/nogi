package kr.co.nogibackend.interfaces.sample;

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
import kr.co.nogibackend.domain.sample.SampleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/samples")
@RequiredArgsConstructor
public class SampleController {

	private final SampleService sampleService;
	private final SampleFacade sampleFacade;

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

}
