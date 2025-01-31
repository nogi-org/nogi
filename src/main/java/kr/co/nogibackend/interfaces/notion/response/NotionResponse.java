package kr.co.nogibackend.interfaces.notion.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionResponse<T> {

  private String object;
  private List<T> results;
  private String next_cursor;
  private String has_more;
  private String type;
  private Object block;

}
