package kr.co.nogibackend.domain.notion.dto.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import kr.co.nogibackend.domain.notion.dto.constant.NotionColor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionNogiCategoryProperty extends NotionNogiCommonProperty {

  private List<NotionMultiSelectProperty> multi_select;

  public static NotionNogiCategoryProperty of(
      String name
      , NotionColor color
  ) {
    NotionMultiSelectProperty multiSelect =
        NotionMultiSelectProperty.of(name, color.getName());

    NotionNogiCategoryProperty notionNogiCategoryProperty = new NotionNogiCategoryProperty();
    notionNogiCategoryProperty.setMulti_select(List.of(multiSelect));

    return notionNogiCategoryProperty;
  }

}
