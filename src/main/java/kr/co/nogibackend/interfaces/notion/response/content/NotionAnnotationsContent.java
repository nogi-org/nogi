package kr.co.nogibackend.interfaces.notion.response.content;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionAnnotationsContent {

  private boolean bold;
  private boolean italic;
  private boolean strikethrough;
  private boolean underline;
  private boolean code;
  private String color;

}
