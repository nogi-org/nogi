package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionCalloutContent {

  private List<NotionRichTextContent> rich_text;
  private NotionEmojiProperty icon;
  private String color;

}
