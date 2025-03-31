package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionNogiStatusProperty extends NotionNogiCommonProperty {

  private NotionStatusProperty select;

}
