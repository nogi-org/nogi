package kr.co.nogibackend.domain.notion.property;

import kr.co.nogibackend.domain.notion.constant.NotionColor;
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

  public String getName() {
    return select.getName();
  }

}
