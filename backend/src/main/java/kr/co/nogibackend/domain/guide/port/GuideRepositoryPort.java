package kr.co.nogibackend.domain.guide.port;

import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.guide.command.GuideRegisterCommand;
import kr.co.nogibackend.domain.guide.entity.Guide;

public interface GuideRepositoryPort {

	Guide registerGuide(GuideRegisterCommand command);

	Optional<Guide> findByStep(int order);

	List<Guide> findGuides();

	Optional<Guide> findById(Long guideId);

	void deleteById(Long guideId);

}
