package kr.co.nogibackend.domain.notion.dto.property;

import kr.co.nogibackend.domain.notion.dto.content.NotionSelectResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionNogiCategoryProperty extends NotionNogiProperty {

	private NotionSelectResponse select;

}
