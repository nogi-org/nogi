package kr.co.nogibackend.domain.notion.dto.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/*
reference:
https://developers.notion.com/reference/rich-text#text
 */
@Getter
@Builder
public class NotionTextContent {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String content;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private NotionLinkContent link;

	public static NotionTextContent buildContent(String content) {
		return
				NotionTextContent
						.builder()
						.content(content)
						.build();
	}

	public static NotionTextContent buildLinkContent(String content, String url) {
		return
				NotionTextContent
						.builder()
						.content(content)
						.link(NotionLinkContent.builder().url(url).build())
						.build();
	}

}
