package kr.co.nogibackend.domain.notion.dto.info;

import java.time.LocalDateTime;
import kr.co.nogibackend.domain.notion.dto.content.NotionBookmarkContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionCalloutContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionChildDatabaseContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionCodeContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionEmbedContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionEquationContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionFileContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionHeadingContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionImageContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionLinkPreviewContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionListItemContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionPDFContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionParagraphContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionQuoteContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableOfContents;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableRowContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTodoContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionToggleBlocksContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionVideoContent;
import kr.co.nogibackend.domain.notion.dto.property.NotionCreatedByProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionLastEditedByProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionParentProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionBlockInfo {

	// common
	private String object;
	private String id;
	private NotionParentProperty parent;
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
	private NotionListItemContent bulleted_list_item;
	private NotionListItemContent numbered_list_item;
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
	private NotionParagraphContent paragraph;
	private NotionPDFContent pdf;
	private NotionQuoteContent quote;
	private NotionTodoContent to_do;
	private NotionToggleBlocksContent toggle;
	private NotionVideoContent video;
	private NotionChildDatabaseContent child_database;
	private NotionTableContent table;
	private NotionTableRowContent table_row;
	private NotionTableOfContents table_of_contents;

}
