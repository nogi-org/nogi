package kr.co.nogibackend.interfaces.notion.response.content;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionDateResponse {

  private String start;
  private String end;
  private String time_zone;
}
