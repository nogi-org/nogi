package kr.co.nogibackend.domain.guide.dto.command;

public record GuideUpdateCommand(
    Long guideId,
    String image,
    String content,
    Integer step
) {

}
