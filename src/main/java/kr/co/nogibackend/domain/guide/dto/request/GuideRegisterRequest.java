package kr.co.nogibackend.domain.guide.dto.request;

import jakarta.validation.constraints.NotBlank;
import kr.co.nogibackend.domain.guide.dto.command.GuideRegisterCommand;

public record GuideRegisterRequest(
	@NotBlank(message = "이미지는 비어 있거나 공백일 수 없습니다.")
	String image,

	@NotBlank(message = "내용은 비어 있거나 공백일 수 없습니다.")
	String content
) {
	public GuideRegisterCommand toCommand() {
		return new GuideRegisterCommand(image, content);
	}

}
