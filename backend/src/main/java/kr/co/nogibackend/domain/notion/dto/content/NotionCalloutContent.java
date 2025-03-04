package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionCalloutContent {

  private List<NotionRichTextContent> rich_text;
  private NotionEmojiProperty icon;
  private String color;

}
