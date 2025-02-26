package kr.co.nogibackend.domain.guide.dto.response;

import kr.co.nogibackend.domain.guide.Guide;

public record GuideRegisterResponse(
    Long guideId
    , String image
    , String content
) {

  public static GuideRegisterResponse of(Guide guide) {
    return new GuideRegisterResponse(guide.getId(), guide.getImage(), guide.getContent());
  }

}
