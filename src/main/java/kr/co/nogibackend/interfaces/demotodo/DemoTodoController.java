package kr.co.nogibackend.interfaces.demotodo;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

// TODO: 프론트 페이지 작업 완료 후 demotodo 패키지 하위 클래스들 삭제
@CrossOrigin(origins = "${cors.allowed-origins}")
@RestController
@RequestMapping("/demo/todos")
@RequiredArgsConstructor
public class DemoTodoController {

	private final DemoTodoJpaRepository demoTodoJpaRepository;

	@GetMapping
	public List<DemoTodo> findDemoTodos() {
		return demoTodoJpaRepository.findAll();
	}

	@PostMapping
	public DemoTodo saveDemoTodos(@RequestBody DemoTodo request) {
		return demoTodoJpaRepository.save(request);
	}

	@DeleteMapping("/{id}")
	public void deleteDemoTodos(@PathVariable Long id) {
		demoTodoJpaRepository.deleteById(id);
	}
}
