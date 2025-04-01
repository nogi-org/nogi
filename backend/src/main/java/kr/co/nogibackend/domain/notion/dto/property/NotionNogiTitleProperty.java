package kr.co.nogibackend.domain.notion.dto.property;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionNogiTitleProperty extends NotionNogiCommonProperty {

	private List<NotionRichTextContent> title;

	public static NotionNogiTitleProperty buildTitles(List<String> titles) {

		List<NotionRichTextContent> richTexts =
				titles
						.stream()
						.map(NotionRichTextContent::buildText)
						.toList();

		return
				NotionNogiTitleProperty
						.builder()
						.title(richTexts)
						.build();
	}

}
