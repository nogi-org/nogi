package kr.co.nogibackend.infra.sample;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.co.nogibackend.domain.sample.Sample;
import kr.co.nogibackend.domain.sample.SampleRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SampleRepositoryImpl implements SampleRepository {

	private final SampleJpaRepository sampleJpaRepository;

	@Override
	public void save(Sample sample) {
		sampleJpaRepository.save(sample);
	}

	@Override
	public Optional<Sample> findById(Long id) {
		return sampleJpaRepository.findById(id);
	}

	@Override
	public void delete(Sample sample) {
		sampleJpaRepository.delete(sample);
	}

	@Override
	public List<Sample> findAll() {
		return sampleJpaRepository.findAll();
	}
}
