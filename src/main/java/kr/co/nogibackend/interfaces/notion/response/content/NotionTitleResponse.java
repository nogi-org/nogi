package kr.co.nogibackend.interfaces.notion.response.content;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionTitleResponse {

  private String type;
  private NotionTextResponse text;
  private NotionAnnotationsContent annotations;
  private String plain_text;
  private String href;

}
