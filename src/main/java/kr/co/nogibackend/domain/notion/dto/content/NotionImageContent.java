package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;

import kr.co.nogibackend.domain.notion.dto.property.NotionFileProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionImageContent {

	private String type;
	private NotionFileProperty file;
	private List<NotionRichTextContent> caption;

}
