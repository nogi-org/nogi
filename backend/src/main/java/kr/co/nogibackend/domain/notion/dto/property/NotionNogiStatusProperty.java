package kr.co.nogibackend.domain.notion.dto.property;

import kr.co.nogibackend.domain.notion.dto.constant.NotionColor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionNogiStatusProperty extends NotionNogiCommonProperty {

  private NotionStatusProperty select;

  public static NotionNogiStatusProperty of(
      String name
      , NotionColor color
  ) {
    NotionNogiStatusProperty notionNogiStatusProperty = new NotionNogiStatusProperty();
    notionNogiStatusProperty.setSelect(NotionStatusProperty.of(name, color.getName()));

    return notionNogiStatusProperty;
  }

}
