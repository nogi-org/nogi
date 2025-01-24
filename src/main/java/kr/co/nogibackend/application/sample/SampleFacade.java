package kr.co.nogibackend.application.sample;

import org.springframework.stereotype.Service;

import kr.co.nogibackend.domain.sample.SampleService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleFacade {
	private final SampleService sampleService;

	// 2가지 이상의 도메인이 사용되어야할 때 사용
}
