package kr.co.nogibackend.infra.guide;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.nogibackend.domain.guide.Guide;

public interface GuideJpaRepository extends JpaRepository<Guide, Long> {
}
