package kr.co.nogibackend.interfaces.notion.response.property;

import kr.co.nogibackend.interfaces.notion.response.content.NotionDateResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionTTTCommitDateResponse {

  private String id;
  private String type;
  private NotionDateResponse date;

}

/*
"Task due date" {
  "id": "AJP%7D",
  "name": "Task due date",
  "type": "date",
  "date": {}
}

 */