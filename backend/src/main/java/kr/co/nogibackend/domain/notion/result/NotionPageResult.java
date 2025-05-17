package kr.co.nogibackend.domain.notion.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import kr.co.nogibackend.domain.notion.property.NotionCoverProperty;
import kr.co.nogibackend.domain.notion.property.NotionCreatedByProperty;
import kr.co.nogibackend.domain.notion.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.property.NotionLastEditedByProperty;
import kr.co.nogibackend.domain.notion.property.NotionNogiProperties;
import kr.co.nogibackend.domain.notion.property.NotionParentProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionPageResult {

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
	private NotionParentProperty parent;
	private String url;
	private String public_url;

}
