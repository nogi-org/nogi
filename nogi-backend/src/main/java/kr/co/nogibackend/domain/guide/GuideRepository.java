package kr.co.nogibackend.domain.guide;

import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.guide.dto.command.GuideRegisterCommand;

public interface GuideRepository {

  Guide registerGuide(GuideRegisterCommand command);

  Optional<Guide> findByStep(int order);

  List<Guide> findGuides();

  Optional<Guide> findById(Long guideId);

  void deleteById(Long guideId);

}
