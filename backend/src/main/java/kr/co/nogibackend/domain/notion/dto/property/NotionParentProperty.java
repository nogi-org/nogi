package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotionParentProperty {

  private String type;
  private String database_id;
  private String page_id;
  private String block_id;

}
