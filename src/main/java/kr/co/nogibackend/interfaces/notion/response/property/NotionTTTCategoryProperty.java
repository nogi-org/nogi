package kr.co.nogibackend.interfaces.notion.response.property;

import kr.co.nogibackend.interfaces.notion.response.content.NotionSelectResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionTTTCategoryProperty {

  private String id;
  private String type;
  private NotionSelectResponse select;

}
