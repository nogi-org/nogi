package kr.co.nogibackend.domain.notion.dto.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionMultiSelectProperty {

  private String id;
  private String name;
  private String color;

  public static NotionMultiSelectProperty of(String name, String color) {
    NotionMultiSelectProperty notionMultiSelectProperty = new NotionMultiSelectProperty();
    notionMultiSelectProperty.setName(name);
    notionMultiSelectProperty.setColor(color);

    return notionMultiSelectProperty;
  }

}
