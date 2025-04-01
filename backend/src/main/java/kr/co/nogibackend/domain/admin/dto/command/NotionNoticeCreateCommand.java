package kr.co.nogibackend.domain.admin.dto.command;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.content.NotionCalloutContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionLinkContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTextContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;

public record NotionNoticeCreateCommand(
		String title,
		String url,
		String content
) {

	// todo: 메소드 불러와서 사용하기
	public List<NotionBlockInfo> createContent() {
		List<NotionRichTextContent> richTexts =
				List.of(
						NotionRichTextContent
								.builder()
								.type("text")
								.text(
										NotionTextContent
												.builder()
												.content(this.content)
												.link(NotionLinkContent.builder().url(this.url).build())
												.build()
								)
								.build()
				);

		return
				List.of(
						NotionBlockInfo
								.builder()
								.object("block")
								.type(NotionBlockInfo.CALL_OUT)
								.callout(
										NotionCalloutContent
												.builder()
												.icon(NotionEmojiProperty.builder().type("emoji").emoji("\uD83D\uDCA1")
														.build())
												.rich_text(richTexts)
												.build()
								)
								.build()
				);
	}

}
