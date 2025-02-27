package kr.co.nogibackend.infra.guide;

import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.guide.Guide;
import kr.co.nogibackend.domain.guide.GuideRepository;
import kr.co.nogibackend.domain.guide.dto.command.GuideRegisterCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

  @Override
  public List<Guide> findGuides() {
    return guideJpaRepository.findAllByOrderByStepAsc();
  }

  @Override
  public Optional<Guide> findById(Long guideId) {
    return guideJpaRepository.findById(guideId);
  }

  @Override
  public void deleteById(Long guideId) {
    guideJpaRepository.deleteById(guideId);
  }

}
