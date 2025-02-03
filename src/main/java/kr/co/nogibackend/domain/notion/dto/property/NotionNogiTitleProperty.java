package kr.co.nogibackend.domain.notion.dto.property;

import java.util.List;

import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionNogiTitleProperty extends NotionNogiProperty {

	private List<NotionRichTextContent> title;

}
