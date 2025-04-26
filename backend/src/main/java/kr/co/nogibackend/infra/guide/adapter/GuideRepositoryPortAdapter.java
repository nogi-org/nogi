package kr.co.nogibackend.infra.guide.adapter;

import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.guide.command.GuideRegisterCommand;
import kr.co.nogibackend.domain.guide.entity.Guide;
import kr.co.nogibackend.domain.guide.port.GuideRepositoryPort;
import kr.co.nogibackend.infra.guide.jpa.GuideJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GuideRepositoryPortAdapter implements GuideRepositoryPort {

	private final GuideJpa guideJpa;

	@Override
	public Guide registerGuide(GuideRegisterCommand command) {
		return guideJpa.save(command.toGuideEntity());
	}

	@Override
	public Optional<Guide> findByStep(int step) {
		return guideJpa.findByStep(step);
	}

	@Override
	public List<Guide> findGuides() {
		return guideJpa.findAllByOrderByStepAsc();
	}

	@Override
	public Optional<Guide> findById(Long guideId) {
		return guideJpa.findById(guideId);
	}

	@Override
	public void deleteById(Long guideId) {
		guideJpa.deleteById(guideId);
	}

}
