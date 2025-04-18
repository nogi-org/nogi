package kr.co.nogibackend.domain.notion.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionStatusProperty {

  private String id;
  private String name;
  private String color;

  public static NotionStatusProperty of(String name, String color) {
    NotionStatusProperty notionStatusProperty = new NotionStatusProperty();
    notionStatusProperty.setName(name);
    notionStatusProperty.setColor(color);
    return notionStatusProperty;
  }

}
