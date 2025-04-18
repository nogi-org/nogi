package kr.co.nogibackend.domain.notion.content;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionBookmarkContent {

  private List<NotionRichTextContent> caption;
  private String url;

}
