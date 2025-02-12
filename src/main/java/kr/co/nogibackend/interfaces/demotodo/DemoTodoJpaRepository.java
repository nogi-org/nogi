package kr.co.nogibackend.interfaces.demotodo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoTodoJpaRepository extends JpaRepository<DemoTodo, Long> {
}
