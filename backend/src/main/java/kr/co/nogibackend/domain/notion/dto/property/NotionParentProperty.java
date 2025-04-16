package kr.co.nogibackend.domain.notion.dto.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// todo: builder 패턴 삭제하기
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
      property.type(type.value).database_id(id);
    } else if (type.equals(PARENT_TYPE.PAGE)) {
      property.type(type.value).page_id(id);
    } else if (type.equals(PARENT_TYPE.BLOCK)) {
      property.type(type.value).page_id(id);
    }
    return property.build();
  }

  public enum PARENT_TYPE {
    DATABASE("database_id"), PAGE("page_id"), BLOCK("block_id");

    private final String value;

    PARENT_TYPE(String value) {
      this.value = value;
    }
  }

}
