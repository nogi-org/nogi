package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionParentProperty {

	private String type;
	private String database_id;
	private String page_id;

}
