package kr.co.nogibackend.domain.notion.result;

import kr.co.nogibackend.domain.notion.property.NotionParentProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionDatabaseResult {

	private String object;
	private String id;
	private NotionParentProperty parent;

}
