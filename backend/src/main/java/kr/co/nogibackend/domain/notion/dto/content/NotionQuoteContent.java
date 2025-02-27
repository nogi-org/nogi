package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
  Package Name : kr.co.nogibackend.interfaces.notion.response.content
  File Name    : NotionQuoteContent
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Getter
@Setter
@ToString
public class NotionQuoteContent {

  private List<NotionRichTextContent> rich_text;
  private String color;

}
