package kr.co.nogibackend.infra.guide;

import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.guide.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideJpaRepository extends JpaRepository<Guide, Long> {

  Optional<Guide> findByStep(int step);

  List<Guide> findAllByOrderByStepAsc();

}
