package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionStatusProperty {

  private String id;
  private String name;
  private String color;
}
