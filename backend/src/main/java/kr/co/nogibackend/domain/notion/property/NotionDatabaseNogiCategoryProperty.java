package kr.co.nogibackend.domain.notion.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionDatabaseNogiCategoryProperty extends NotionNogiCommonProperty {

	private NotionMultiSelectOptionsProperty multi_select;

}
