package kr.co.nogibackend.domain.notion.content;

import java.util.List;
import kr.co.nogibackend.domain.notion.property.NotionFileProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionFileContent {

  private List<NotionRichTextContent> caption;
  private String type;
  private NotionFileProperty external;
  private String name;

}
