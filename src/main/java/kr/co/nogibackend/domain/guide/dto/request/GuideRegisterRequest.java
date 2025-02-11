package kr.co.nogibackend.domain.guide.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import kr.co.nogibackend.domain.guide.dto.command.GuideRegisterCommand;

public record GuideRegisterRequest(
	@NotBlank(message = "이미지는 비어 있거나 공백일 수 없습니다.")
	String image,

	@NotBlank(message = "내용은 비어 있거나 공백일 수 없습니다.")
	String content,

	// todo: @Min annotation 동작 안함
	@Min(value = 0, message = "순서는 0 이상만 가능합니다.")
	int step
) {
	public GuideRegisterCommand toCommand() {
		return new GuideRegisterCommand(image, content, step);
	}

}
