package kr.co.nogibackend.domain.guide;

import static kr.co.nogibackend.response.code.GuideResponseCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.guide.dto.command.GuideRegisterCommand;
import kr.co.nogibackend.domain.guide.dto.response.GuideRegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuideService {

	private final GuideRepository guideRepository;

	@Transactional
	public GuideRegisterResponse registerGuide(GuideRegisterCommand command) {
		this.validateStep(command.step());
		return GuideRegisterResponse.of(guideRepository.registerGuide(command));
	}

	// 중복된 step 가 존재하는지 확인
	private void validateStep(int step) {
		guideRepository
			.findByStep(step)
			.ifPresent(existingGuide -> {
				throw new GlobalException(F_DUPLICATION_ORDER);
			});
	}

}
