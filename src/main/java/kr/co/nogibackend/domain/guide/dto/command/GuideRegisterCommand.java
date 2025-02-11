package kr.co.nogibackend.domain.guide.dto.command;

import kr.co.nogibackend.domain.guide.Guide;
import kr.co.nogibackend.domain.user.User;

public record GuideRegisterCommand(
	String image,
	String content,
	int step
) {

	public Guide toGuideEntity() {
		return
			Guide
				.builder()
				.image(image)
				.content(content)
				.step(step)
				.user(User.builder().id(1233L).build())
				.build();
	}

}
