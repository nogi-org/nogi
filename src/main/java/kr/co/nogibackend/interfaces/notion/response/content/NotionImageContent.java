package kr.co.nogibackend.interfaces.notion.response.content;

import java.util.List;

import kr.co.nogibackend.interfaces.notion.response.property.NotionFileProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionImageContent {

	private String type;
	private NotionFileProperty external;
	private List<NotionRichTextContent> caption;

}
