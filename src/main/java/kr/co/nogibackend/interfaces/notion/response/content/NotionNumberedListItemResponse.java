package kr.co.nogibackend.interfaces.notion.response.content;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionNumberedListItemResponse {

  private List<NotionRichTextContent> rich_text = new ArrayList<>();
  private String color;

}
