package kr.co.nogibackend.domain.notion.dto.property;

import kr.co.nogibackend.domain.notion.dto.content.NotionAnnotationsContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTextContent;
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
