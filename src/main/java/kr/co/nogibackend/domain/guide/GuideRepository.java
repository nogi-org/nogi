package kr.co.nogibackend.domain.guide;

import java.util.Optional;

import kr.co.nogibackend.domain.guide.dto.command.GuideRegisterCommand;

public interface GuideRepository {

	Guide registerGuide(GuideRegisterCommand command);

	Optional<Guide> findByStep(int order);

}
