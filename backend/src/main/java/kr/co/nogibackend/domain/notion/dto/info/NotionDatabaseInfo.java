package kr.co.nogibackend.domain.notion.dto.info;

import kr.co.nogibackend.domain.notion.dto.property.NotionParentProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionDatabaseInfo {

	private String object;
	private String id;
	private NotionParentProperty parent;

}
