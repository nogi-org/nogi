package kr.co.nogibackend.interfaces.notion.response.property;

import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.interfaces.notion.response.content.NotionTitleResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionTTTTitleResponse {

  private String id;
  private String type;
  private List<NotionTitleResponse> title = new ArrayList<>();

}
