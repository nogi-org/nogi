package kr.co.nogibackend.domain.notion.dto.content;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionRichTextContent {

	private String type;
	private NotionTextContent text;
	private NotionAnnotationsContent annotations;
	private String plain_text;
	private String href;

	public boolean emptyText() {
		return text == null;
	}

	public boolean emptyLink() {
		return text.getLink() == null;
	}

}
