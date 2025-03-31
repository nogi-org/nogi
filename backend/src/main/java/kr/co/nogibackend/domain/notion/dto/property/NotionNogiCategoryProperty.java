package kr.co.nogibackend.domain.notion.dto.property;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionNogiCategoryProperty extends NotionNogiCommonProperty {

  private List<NotionMultiSelectProperty> multi_select;

}
