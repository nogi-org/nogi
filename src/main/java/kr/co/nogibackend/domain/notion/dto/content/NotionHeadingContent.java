package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionHeadingContent {

	private List<NotionRichTextContent> rich_text;
	private boolean is_toggleable;
	private String color;

}
