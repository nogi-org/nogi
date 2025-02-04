package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;

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

	public static String mergePlainText(List<NotionRichTextContent> richText, boolean isOneLine) {
		StringBuilder strb = new StringBuilder();
		for (NotionRichTextContent text : richText) {
			// isOneLine 가 true 면 줄바꿈을 한줄로 합침
			String str = isOneLine ? text.getPlain_text().replaceAll("\\r?\\n", " ") : text.getPlain_text();
			strb.append(str);
		}
		return strb.toString();
	}

}
