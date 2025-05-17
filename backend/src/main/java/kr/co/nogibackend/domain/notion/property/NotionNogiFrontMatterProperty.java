package kr.co.nogibackend.domain.notion.property;

import java.util.List;
import kr.co.nogibackend.domain.notion.content.NotionRichTextContent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionNogiFrontMatterProperty extends NotionNogiCommonProperty {

	private List<NotionRichTextContent> rich_text;

	// todo: 필요없으면 삭제
//	public static NotionNogiFrontMatterProperty of(List<String> titles) {
//
//		List<NotionRichTextContent> richTexts =
//				titles
//						.stream()
//						.map(NotionRichTextContent::buildText)
//						.toList();
//
//		NotionNogiFrontMatterProperty notionNogiTitleProperty = new NotionNogiFrontMatterProperty();
//		notionNogiTitleProperty.setTitle(richTexts);
//
//		return notionNogiTitleProperty;
//	}

}
