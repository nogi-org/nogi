package kr.co.nogibackend.interfaces.notion.response;

import java.time.LocalDateTime;

import kr.co.nogibackend.interfaces.notion.response.content.NotionBookmarkContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionBulletedListItemContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionCalloutContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionCodeContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionEmbedContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionEquationContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionFileContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionHeadingContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionImageContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionLastEditedByProperty;
import kr.co.nogibackend.interfaces.notion.response.content.NotionLinkPreviewContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionNumberedListItemContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionPDFContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionParagraphContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionParentResponse;
import kr.co.nogibackend.interfaces.notion.response.content.NotionQuoteContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionTodoContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionToggleBlocksContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionVideoContent;
import kr.co.nogibackend.interfaces.notion.response.property.NotionCreatedByProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionBlockResponse {

	// common
	private String object;
	private String id;
	private NotionParentResponse parent;
	private String type;
	private LocalDateTime created_time;
	private NotionCreatedByProperty created_by;
	private LocalDateTime last_edited_time;
	private NotionLastEditedByProperty last_edited_by;
	private boolean archived;
	private boolean in_trash;
	private boolean has_children;

	// 개별 block (type 에 따라 값을 가짐)
	private NotionBookmarkContent bookmark;
	private NotionBulletedListItemContent bulleted_list_item;
	private NotionCalloutContent callout;
	private NotionCodeContent code;
	private NotionEmbedContent embed;
	private NotionEquationContent equation;
	private NotionFileContent file;
	private NotionHeadingContent heading_1;
	private NotionHeadingContent heading_2;
	private NotionHeadingContent heading_3;
	private NotionImageContent image;
	private NotionLinkPreviewContent link_preview;
	private NotionNumberedListItemContent numbered_list_item;
	private NotionParagraphContent paragraph;
	private NotionPDFContent pdf;
	private NotionQuoteContent quote;
	private NotionTodoContent to_do;
	private NotionToggleBlocksContent toggle;
	private NotionVideoContent video;

}
