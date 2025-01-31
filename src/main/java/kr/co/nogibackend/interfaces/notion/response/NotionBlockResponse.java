package kr.co.nogibackend.interfaces.notion.response;

import java.time.LocalDateTime;
import kr.co.nogibackend.interfaces.notion.response.content.NotionBulletedListItemContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionCodeContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionHeadingContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionImageContent;
import kr.co.nogibackend.interfaces.notion.response.content.NotionLastEditedByProperty;
import kr.co.nogibackend.interfaces.notion.response.content.NotionNumberedListItemResponse;
import kr.co.nogibackend.interfaces.notion.response.content.NotionParagraphResponse;
import kr.co.nogibackend.interfaces.notion.response.content.NotionParentResponse;
import kr.co.nogibackend.interfaces.notion.response.content.NotionTodoResponse;
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
  private NotionHeadingContent heading_1;
  private NotionHeadingContent heading_2;
  private NotionHeadingContent heading_3;
  private NotionParagraphResponse paragraph;
  private NotionBulletedListItemContent bulleted_list_item;
  private NotionNumberedListItemResponse numbered_list_item;
  private NotionCodeContent code;
  private NotionImageContent image;
  private NotionTodoResponse to_do;

}
