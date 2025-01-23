package kr.co.nogibackend.infra.sample;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.nogibackend.domain.sample.Sample;

public interface SampleJpaRepository extends JpaRepository<Sample, Long> {
}
