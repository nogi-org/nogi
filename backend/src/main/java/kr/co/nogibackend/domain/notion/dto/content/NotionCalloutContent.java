package kr.co.nogibackend.domain.notion.dto.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty.EMOJI_TYPE;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionCalloutContent {

  private List<NotionRichTextContent> rich_text;
  private NotionEmojiProperty icon;
  private String color;

  public static NotionCalloutContent buildEmoji(
      List<NotionRichTextContent> richTexts
      , String emoji
  ) {
    return
        NotionCalloutContent
            .builder()
            .icon(NotionEmojiProperty.buildEmoji(EMOJI_TYPE.EMOJI, emoji))
            .rich_text(richTexts)
            .build();
  }

}
