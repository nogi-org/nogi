package kr.co.nogibackend.infra.guide.jpa;

import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.guide.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideJpa extends JpaRepository<Guide, Long> {

	Optional<Guide> findByStep(int step);

	List<Guide> findAllByOrderByStepAsc();

}
