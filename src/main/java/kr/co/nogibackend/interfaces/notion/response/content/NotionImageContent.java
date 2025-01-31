package kr.co.nogibackend.interfaces.notion.response.content;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionImageContent {

  private String type;
  private NotionFileContent file;
  private List<NotionRichTextContent> caption = new ArrayList<>();

}
