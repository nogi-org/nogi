package kr.co.nogibackend.domain.sample;

import java.util.List;
import java.util.Optional;

public interface SampleRepository {
	void save(Sample sample);

	Optional<Sample> findById(Long id);

	void delete(Sample sample);

	List<Sample> findAll();
}
