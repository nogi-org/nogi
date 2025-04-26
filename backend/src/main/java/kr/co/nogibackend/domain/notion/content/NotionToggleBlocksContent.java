package kr.co.nogibackend.domain.notion.content;

import java.util.List;
import kr.co.nogibackend.domain.notion.result.NotionBlockResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionToggleBlocksContent {

	private List<NotionRichTextContent> rich_text;
	private String color;
	private List<NotionBlockResult> children;

	public boolean hasChildren() {
		return this.children != null && !this.children.isEmpty();
	}

}
