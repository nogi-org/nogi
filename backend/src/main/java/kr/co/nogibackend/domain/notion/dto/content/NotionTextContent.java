package kr.co.nogibackend.domain.notion.dto.content;

import lombok.Builder;
import lombok.Getter;

/*
reference:
https://developers.notion.com/reference/rich-text#text
 */
@Getter
@Builder
public class NotionTextContent {

	private String content;
	private NotionLinkContent link;

	public static NotionTextContent buildContent(String content) {
		return
				NotionTextContent
						.builder()
						.content(content)
						.build();
	}

}
