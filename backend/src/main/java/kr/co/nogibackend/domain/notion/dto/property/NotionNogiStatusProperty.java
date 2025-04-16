package kr.co.nogibackend.domain.notion.dto.property;

import kr.co.nogibackend.domain.notion.dto.constant.NotionColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotionNogiStatusProperty extends NotionNogiCommonProperty {

  private NotionStatusProperty select;

  public static NotionNogiStatusProperty buildColorStatus(
      String name
      , NotionColor color
  ) {
    return
        new NotionNogiStatusProperty(NotionStatusProperty.buildColorName(name, color.getName()));
  }

}
