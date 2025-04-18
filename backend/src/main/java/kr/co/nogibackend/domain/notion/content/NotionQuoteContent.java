package kr.co.nogibackend.domain.notion.content;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionQuoteContent {

  private List<NotionRichTextContent> rich_text;
  private String color;

}
