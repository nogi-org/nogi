package kr.co.nogibackend.domain.notion.dto.info;

import java.time.LocalDateTime;

import kr.co.nogibackend.domain.notion.dto.content.NotionLastEditedByProperty;
import kr.co.nogibackend.domain.notion.dto.content.NotionParentResponse;
import kr.co.nogibackend.domain.notion.dto.property.NotionCoverProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionCreatedByProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionNogiProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionPageInfo {

	private String object;
	private String id;
	private LocalDateTime created_time;
	private NotionCreatedByProperty created_by;
	private LocalDateTime last_edited_time;
	private NotionLastEditedByProperty last_edited_by;
	private boolean archived;
	private boolean in_trash;
	private NotionEmojiProperty icon;
	private NotionCoverProperty cover;
	private NotionNogiProperties properties;
	private NotionParentResponse parent;
	private String url;
	private String public_url;

}
