package kr.co.nogibackend.domain.guide.dto.response;

import kr.co.nogibackend.domain.guide.Guide;

public record GuideUpdateResponse(
	Long guideId
	, String image
	, String content
	, Integer step
) {

	public static GuideUpdateResponse of(Guide guide) {
		return new GuideUpdateResponse(guide.getId(), guide.getImage(), guide.getContent(), guide.getStep());
	}

}
