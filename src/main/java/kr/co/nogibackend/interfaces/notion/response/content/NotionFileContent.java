package kr.co.nogibackend.interfaces.notion.response.content;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionFileContent {

  private String url;
  private LocalDateTime expiry_time;

}
