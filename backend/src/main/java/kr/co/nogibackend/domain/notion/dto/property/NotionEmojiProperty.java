package kr.co.nogibackend.domain.notion.dto.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionEmojiProperty {

  private String type;
  private String emoji;

  public static NotionEmojiProperty buildEmoji(EMOJI_TYPE type, String emoji) {
    NotionEmojiProperty notionEmojiProperty = new NotionEmojiProperty();
    notionEmojiProperty.setType(type.value);
    notionEmojiProperty.setEmoji(emoji);

    return notionEmojiProperty;
  }

  public enum EMOJI_TYPE {
    EMOJI("emoji"), ICON("icon");

    private final String value;

    EMOJI_TYPE(String value) {
      this.value = value;
    }
  }

}
