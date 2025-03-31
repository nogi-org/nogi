package kr.co.nogibackend.domain.notion.dto.info;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotionInfo<T> {

  private String object;
  private List<T> results;
  private String next_cursor;
  private boolean has_more;
  private String type;
  private Object block;
  private String developer_survey;
  private String request_id;

}
