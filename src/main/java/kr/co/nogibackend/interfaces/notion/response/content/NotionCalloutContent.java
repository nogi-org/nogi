package kr.co.nogibackend.interfaces.notion.response.content;

import java.util.List;

import kr.co.nogibackend.interfaces.notion.response.property.NotionEmojiProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
  Package Name : kr.co.nogibackend.interfaces.notion.response.content
  File Name    : NotionCalloutContent
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Getter
@Setter
@ToString
public class NotionCalloutContent {

	private List<NotionRichTextContent> rich_text;
	private NotionEmojiProperty icon;
	private String color;

}
