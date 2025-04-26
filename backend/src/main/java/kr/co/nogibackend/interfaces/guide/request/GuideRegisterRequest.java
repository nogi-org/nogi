package kr.co.nogibackend.interfaces.guide.request;

import kr.co.nogibackend.domain.guide.command.GuideRegisterCommand;

public record GuideRegisterRequest(
		// @NotBlank(message = "이미지는 비어 있거나 공백일 수 없습니다.")
		String image,

		// @NotBlank(message = "내용은 비어 있거나 공백일 수 없습니다.")
		String content,

		// @Min(value = 1, message = "순서는 1 이상만 가능합니다.")
		Integer step
) {

	public GuideRegisterCommand toCommand() {
		return new GuideRegisterCommand(image, content, step);
	}
}
