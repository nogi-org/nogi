package kr.co.nogibackend.domain.sample;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SampleService {

	private final SampleRepository sampleRepository;

	public List<SampleInfo.Data> findAll() {
		return sampleRepository.findAll().stream()
			.map(SampleInfo.Data::from)
			.toList();
	}

	public SampleInfo.Data findById(Long id) {
		return sampleRepository.findById(id)
			.map(SampleInfo.Data::from)
			.orElseThrow(() -> new IllegalArgumentException("해당 데이터가 없습니다."));
	}

	public void create(SampleCommand.Create command) {
		Sample sample = Sample.builder()
			.column1(command.column1())
			.column2(command.column2())
			.build();
		sampleRepository.save(sample);
	}

	public void update(Long id, SampleCommand.Update command) {
		Sample sample = sampleRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 데이터가 없습니다."));

		sample.update(command.column1(), command.column2());
	}

	public void delete(Long id) {
		Sample sample = sampleRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 데이터가 없습니다."));
		sampleRepository.delete(sample);
	}
}
