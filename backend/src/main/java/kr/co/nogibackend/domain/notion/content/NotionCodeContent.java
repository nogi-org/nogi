package kr.co.nogibackend.domain.notion.content;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionCodeContent {

  private List<NotionRichTextContent> caption;
  private List<NotionRichTextContent> rich_text;
  private String language;

}
