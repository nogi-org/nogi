package kr.co.nogibackend.interfaces.notion.response.property;

import kr.co.nogibackend.interfaces.notion.response.content.NotionAnnotationsContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionTextContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionTitleProperty {

	private String type;
	private NotionTextContent text;
	private NotionAnnotationsContent annotations;
	private String plain_text;
	private String href;

}
