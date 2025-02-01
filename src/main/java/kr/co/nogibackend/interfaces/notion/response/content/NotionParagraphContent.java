package kr.co.nogibackend.interfaces.notion.response.content;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class NotionParagraphContent {

	private List<NotionRichTextContent> rich_text;
	private String color;

}
