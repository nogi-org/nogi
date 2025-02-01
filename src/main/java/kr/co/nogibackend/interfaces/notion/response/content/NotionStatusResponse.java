package kr.co.nogibackend.interfaces.notion.response.content;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionStatusResponse {

  private String id;
  private String name;
  private String color;
}
