package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionHeadingContent {

  private List<NotionRichTextContent> rich_text;
  private boolean is_toggleable;
  private String color;

}
