package kr.co.nogibackend.domain.notion.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionHeadingContent {

  private List<NotionRichTextContent> rich_text;
  @JsonProperty("is_toggleable")
  private boolean is_toggleable;
  private String color;

}
