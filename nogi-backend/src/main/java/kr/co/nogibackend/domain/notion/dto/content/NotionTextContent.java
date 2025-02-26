package kr.co.nogibackend.domain.notion.dto.content;

import lombok.Getter;
import lombok.ToString;

/*
reference:
https://developers.notion.com/reference/rich-text#text
 */
@Getter
@ToString
public class NotionTextContent {

  private String content;
  private NotionLinkContent link;

}
