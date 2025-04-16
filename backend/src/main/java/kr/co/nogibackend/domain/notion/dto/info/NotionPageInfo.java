package kr.co.nogibackend.domain.notion.dto.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import kr.co.nogibackend.domain.notion.dto.property.NotionCoverProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionCreatedByProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionEmojiProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionLastEditedByProperty;
import kr.co.nogibackend.domain.notion.dto.property.NotionNogiProperties;
import kr.co.nogibackend.domain.notion.dto.property.NotionParentProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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
  private NotionParentProperty parent;
  private String url;
  private String public_url;

}
