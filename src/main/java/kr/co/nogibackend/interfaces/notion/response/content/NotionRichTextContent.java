package kr.co.nogibackend.interfaces.notion.response.content;

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

}
