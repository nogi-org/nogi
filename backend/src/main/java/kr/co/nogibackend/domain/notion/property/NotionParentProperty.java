package kr.co.nogibackend.domain.notion.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionParentProperty {

  private String type;
  private String database_id;
  private String page_id;
  private String block_id;

  public static NotionParentProperty ofParent(
      PARENT_TYPE type
      , String id
  ) {
    NotionParentProperty notionParentProperty = new NotionParentProperty();
    notionParentProperty.setType(type.value);

    if (type.equals(PARENT_TYPE.DATABASE)) {
      notionParentProperty.setDatabase_id(id);
    } else if (type.equals(PARENT_TYPE.PAGE)) {
      notionParentProperty.setPage_id(id);
    } else if (type.equals(PARENT_TYPE.BLOCK)) {
      notionParentProperty.setBlock_id(id);
    }

    return notionParentProperty;
  }

  public enum PARENT_TYPE {
    DATABASE("database_id"), PAGE("page_id"), BLOCK("block_id");

    private final String value;

    PARENT_TYPE(String value) {
      this.value = value;
    }
  }

}
