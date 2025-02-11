package kr.co.nogibackend.domain.guide;

import kr.co.nogibackend.domain.guide.dto.command.GuideRegisterCommand;

public interface GuideRepository {

	Guide registerGuide(GuideRegisterCommand command);

}
