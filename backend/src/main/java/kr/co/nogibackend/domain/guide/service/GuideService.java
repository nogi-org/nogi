package kr.co.nogibackend.domain.guide.service;

import static kr.co.nogibackend.global.response.code.GuideResponseCode.F_DUPLICATION_ORDER;
import static kr.co.nogibackend.global.response.code.GuideResponseCode.F_NOT_FOUND_GUIDE;

import java.util.List;
import kr.co.nogibackend.domain.guide.command.GuideRegisterCommand;
import kr.co.nogibackend.domain.guide.command.GuideUpdateCommand;
import kr.co.nogibackend.domain.guide.entity.Guide;
import kr.co.nogibackend.domain.guide.port.GuideRepositoryPort;
import kr.co.nogibackend.global.config.exception.GlobalException;
import kr.co.nogibackend.interfaces.guide.response.GuideRegisterResponse;
import kr.co.nogibackend.interfaces.guide.response.GuideUpdateResponse;
import kr.co.nogibackend.interfaces.guide.response.GuidesGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuideService {

	private final GuideRepositoryPort guideRepositoryPort;

	@Transactional
	public GuideRegisterResponse registerGuide(GuideRegisterCommand command) {
		// todo: security 개발되면 guide 생성 시 user 넣기

		// step 은 중복 불가능
		this.validateStepIfPresentThrow(command.step());
		return GuideRegisterResponse.of(guideRepositoryPort.registerGuide(command));
	}

	public List<GuidesGetResponse> getGuides() {
		return GuidesGetResponse.ofs(guideRepositoryPort.findGuides());
	}

	@Transactional
	public GuideUpdateResponse updateGuide(GuideUpdateCommand command) {
		Guide guide = this.findGuideByIdIfElseThrow(command.guideId());
		guide.updateAll(command.image(), command.content(), command.step());
		return GuideUpdateResponse.of(guide);
	}

	@Transactional
	public Long deleteGuide(Long guideId) {
		// todo: soft delete 로????
		this.findGuideByIdIfElseThrow(guideId);
		guideRepositoryPort.deleteById(guideId);
		return guideId;
	}

	private Guide findGuideByIdIfElseThrow(Long guideId) {
		return
				guideRepositoryPort
						.findById(guideId)
						.orElseThrow(() -> new GlobalException(F_NOT_FOUND_GUIDE));
	}

	// 중복된 step 가 존재하는지 확인
	private void validateStepIfPresentThrow(int step) {
		guideRepositoryPort
				.findByStep(step)
				.ifPresent(existingGuide -> {
					throw new GlobalException(F_DUPLICATION_ORDER);
				});
	}

}
