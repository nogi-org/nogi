package kr.co.nogibackend.infra.guide;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.co.nogibackend.domain.guide.Guide;
import kr.co.nogibackend.domain.guide.GuideRepository;
import kr.co.nogibackend.domain.guide.dto.command.GuideRegisterCommand;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GuideRepositoryImpl implements GuideRepository {

	private final GuideJpaRepository guideJpaRepository;

	@Override
	public Guide registerGuide(GuideRegisterCommand command) {
		return guideJpaRepository.save(command.toGuideEntity());
	}

	@Override
	public Optional<Guide> findByStep(int step) {
		return guideJpaRepository.findByStep(step);
	}

}
