package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// todo: builder 패턴 삭제하기
@Getter
@Builder
@Setter
public class NotionEmojiProperty {

  private String type;
  private String emoji;

  public static NotionEmojiProperty buildEmoji(EMOJI_TYPE type, String emoji) {
    return
        NotionEmojiProperty
            .builder()
            .type(type.value)
            .emoji(emoji)
            .build();
  }

  public enum EMOJI_TYPE {
    EMOJI("emoji"), ICON("icon");

    private final String value;

    EMOJI_TYPE(String value) {
      this.value = value;
    }
  }

}
