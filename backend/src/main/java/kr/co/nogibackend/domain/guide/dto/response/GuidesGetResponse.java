package kr.co.nogibackend.domain.guide.dto.response;

import java.util.List;
import kr.co.nogibackend.domain.guide.Guide;

public record GuidesGetResponse(
    Long guideId,
    String image,
    String content,
    Integer step
) {

  public static List<GuidesGetResponse> ofs(List<Guide> guides) {
    return
        guides
            .stream()
            .map(guide ->
                new GuidesGetResponse(guide.getId(), guide.getImage(), guide.getContent(),
                    guide.getStep())
            )
            .toList();
  }

}
