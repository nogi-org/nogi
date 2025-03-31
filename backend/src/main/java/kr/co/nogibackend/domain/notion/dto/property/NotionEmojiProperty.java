package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotionEmojiProperty {

  private String type;
  private String emoji;

}
