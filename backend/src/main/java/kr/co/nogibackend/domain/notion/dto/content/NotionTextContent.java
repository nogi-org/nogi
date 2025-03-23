package kr.co.nogibackend.domain.notion.dto.content;

import lombok.Getter;
import lombok.Setter;

/*
reference:
https://developers.notion.com/reference/rich-text#text
 */
@Getter
@Setter
public class NotionTextContent {

	private String content;
	private NotionLinkContent link;

}
