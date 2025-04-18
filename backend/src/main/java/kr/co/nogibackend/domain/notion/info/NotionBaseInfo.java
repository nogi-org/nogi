package kr.co.nogibackend.domain.notion.info;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionBaseInfo<T> {

  private String object;
  private List<T> results;
  private String next_cursor;
  private boolean has_more;
  private String type;
  private Object block;
  private String developer_survey;
  private String request_id;

}
