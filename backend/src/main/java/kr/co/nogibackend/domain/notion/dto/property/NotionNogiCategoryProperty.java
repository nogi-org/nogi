package kr.co.nogibackend.domain.notion.dto.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import kr.co.nogibackend.domain.notion.dto.constant.NotionColor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionNogiCategoryProperty extends NotionNogiCommonProperty {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<NotionMultiSelectProperty> multi_select;

	public static NotionNogiCategoryProperty buildColorMultiSelect(
			String name
			, NotionColor color
	) {
		NotionMultiSelectProperty multiSelect =
				NotionMultiSelectProperty.buildColorName(name, color.getName());
		return
				NotionNogiCategoryProperty
						.builder()
						.multi_select(List.of(multiSelect))
						.build();
	}

}
