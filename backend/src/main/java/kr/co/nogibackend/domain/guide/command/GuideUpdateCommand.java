package kr.co.nogibackend.domain.guide.command;

public record GuideUpdateCommand(
		Long guideId,
		String image,
		String content,
		Integer step
) {

}
