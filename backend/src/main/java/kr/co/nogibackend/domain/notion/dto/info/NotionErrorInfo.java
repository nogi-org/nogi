package kr.co.nogibackend.domain.notion.dto.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionErrorInfo {

  private String object;
  private int status;
  private String code;
  private String message;

}
