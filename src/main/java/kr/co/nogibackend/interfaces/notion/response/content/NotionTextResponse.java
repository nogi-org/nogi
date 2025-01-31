package kr.co.nogibackend.interfaces.notion.response.content;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotionTextResponse {

  private String content;
  private NotionLinkResponse link;

}
