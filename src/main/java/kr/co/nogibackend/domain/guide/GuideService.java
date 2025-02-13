package kr.co.nogibackend.domain.guide;

import static kr.co.nogibackend.response.code.GuideResponseCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.guide.dto.command.GuideRegisterCommand;
import kr.co.nogibackend.domain.guide.dto.command.GuideUpdateCommand;
import kr.co.nogibackend.domain.guide.dto.response.GuideRegisterResponse;
import kr.co.nogibackend.domain.guide.dto.response.GuideUpdateResponse;
import kr.co.nogibackend.domain.guide.dto.response.GuidesGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuideService {

	private final GuideRepository guideRepository;

	@Transactional
	public GuideRegisterResponse registerGuide(GuideRegisterCommand command) {
		// todo: security 개발되면 guide 생성 시 user 넣기

		// step 은 중복 불가능
		this.validateStepIfPresentThrow(command.step());
		return GuideRegisterResponse.of(guideRepository.registerGuide(command));
	}

	public List<GuidesGetResponse> getGuides() {
		return GuidesGetResponse.ofs(guideRepository.findGuides());
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
		guideRepository.deleteById(guideId);
		return guideId;
	}

	private Guide findGuideByIdIfElseThrow(Long guideId) {
		return
			guideRepository
				.findById(guideId)
				.orElseThrow(() -> new GlobalException(F_NOT_FOUND_GUIDE));
	}

	// 중복된 step 가 존재하는지 확인
	private void validateStepIfPresentThrow(int step) {
		guideRepository
			.findByStep(step)
			.ifPresent(existingGuide -> {
				throw new GlobalException(F_DUPLICATION_ORDER);
			});
	}

}
