package kr.co.nogibackend.domain.notion.dto.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty.EMOJI_TYPE;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionCalloutContent {

  private List<NotionRichTextContent> rich_text;
  private NotionEmojiProperty icon;
  private String color;

  public static NotionCalloutContent of(
      List<NotionRichTextContent> richTexts
      , String emoji
  ) {
    NotionCalloutContent notionCalloutContent = new NotionCalloutContent();
    notionCalloutContent.setIcon(NotionEmojiProperty.buildEmoji(EMOJI_TYPE.EMOJI, emoji));
    notionCalloutContent.setRich_text(richTexts);

    return notionCalloutContent;
  }

}
