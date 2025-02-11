package kr.co.nogibackend.infra.guide;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.nogibackend.domain.guide.Guide;

public interface GuideJpaRepository extends JpaRepository<Guide, Long> {

	Optional<Guide> findByStep(int step);

}
