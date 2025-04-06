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
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionBlockInfo {

	public final static String HEADING1 = "heading_1";
	public final static String HEADING2 = "heading_2";
	public final static String HEADING3 = "heading_3";
	public final static String PARAGRAPH = "paragraph";
	public final static String CODE = "code";
	public final static String DIVIDER = "divider";
	public final static String TODO = "to_do";
	public final static String IMAGE = "image";
	public final static String BULLETED_LIST = "bulleted_list_item";
	public final static String NUMBERED_LIST = "numbered_list_item";
	public final static String TABLE = "table";
	public final static String TOGGLE = "toggle";
	public final static String QUOTE = "quote";
	public final static String CALL_OUT = "callout";

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
