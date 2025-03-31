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

  public static NotionParentProperty buildParent(
      PARENT_TYPE type
      , String id
  ) {
    NotionParentPropertyBuilder property = NotionParentProperty.builder();
    if (type.equals(PARENT_TYPE.DATABASE)) {
      property.type("database").database_id(id);
    } else if (type.equals(PARENT_TYPE.PAGE)) {
      property.type("page").page_id(id);
    } else if (type.equals(PARENT_TYPE.BLOCK)) {
      property.type("block").page_id(id);
    }
    return property.build();
  }

  public enum PARENT_TYPE {
    DATABASE, PAGE, BLOCK
  }

}
