package kr.co.nogibackend.domain.notion.dto.property;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.constant.NotionColor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionNogiCategoryProperty extends NotionNogiCommonProperty {

  private List<NotionMultiSelectProperty> multi_select;

  public static List<NotionMultiSelectProperty> buildColorMultiSelect(List<> ) {

    NotionMultiSelectProperty.buildColorName("공지", NotionColor.BLUE.getName());
    return
  }

}
