package kr.co.nogibackend.domain.notion.dto.content;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionAnnotationsContent {

  private boolean bold;
  private boolean italic;
  private boolean strikethrough;
  private boolean underline;
  private boolean code;
  private String color;

}
