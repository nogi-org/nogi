package kr.co.nogibackend.interfaces.notion.response.content;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionNumberedListItemContent {

	private List<NotionRichTextContent> rich_text;
	private String color;

}
