package kr.co.nogibackend.domain.guide.dto.command;

import kr.co.nogibackend.domain.guide.Guide;

public record GuideRegisterCommand(
	String image,
	String content
) {

	public Guide toGuideEntity() {
		return
			Guide
				.builder()
				.image(image)
				.content(content)
				.build();
	}

}
