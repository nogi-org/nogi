package kr.co.nogibackend.interfaces.guide.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import kr.co.nogibackend.domain.guide.command.GuideUpdateCommand;

public record GuideUpdateRequest(
		@Min(value = 1, message = "가이드ID는 1 이상만 가능합니다.")
		Long guideId,

		@NotBlank(message = "이미지는 비어 있거나 공백일 수 없습니다.")
		String image,

		@NotBlank(message = "내용은 비어 있거나 공백일 수 없습니다.")
		String content,

		@Min(value = 1, message = "순서는 1 이상만 가능합니다.")
		Integer step
) {

	public GuideUpdateCommand toCommand() {
		return new GuideUpdateCommand(guideId, image, content, step);
	}

}
