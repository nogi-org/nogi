package kr.co.nogibackend.domain.notion.content;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionListItemContent {

  private List<NotionRichTextContent> rich_text;
  private String color;
  private List<NotionListItemContent> children;

}
