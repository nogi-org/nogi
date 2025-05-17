package kr.co.nogibackend.domain.guide.command;

import kr.co.nogibackend.domain.guide.entity.Guide;
import kr.co.nogibackend.domain.user.entity.User;

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
