package kr.co.nogibackend.domain.guide;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nogibackend.domain.guide.dto.command.GuideRegisterCommand;
import kr.co.nogibackend.domain.guide.dto.response.GuideRegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GuideService {

	private final GuideRepository guideRepository;

	public GuideRegisterResponse registerGuide(GuideRegisterCommand command) {
		return GuideRegisterResponse.of(guideRepository.registerGuide(command));
	}

}
