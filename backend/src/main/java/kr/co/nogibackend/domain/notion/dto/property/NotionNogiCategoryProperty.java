package kr.co.nogibackend.domain.notion.dto.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import kr.co.nogibackend.domain.notion.dto.constant.NotionColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionNogiCategoryProperty extends NotionNogiCommonProperty {

  private List<NotionMultiSelectProperty> multi_select;

  public static NotionNogiCategoryProperty buildColorMultiSelect(
      String name
      , NotionColor color
  ) {
    NotionMultiSelectProperty multiSelect =
        NotionMultiSelectProperty.buildColorName(name, color.getName());

    return new NotionNogiCategoryProperty(List.of(multiSelect));
  }

}
