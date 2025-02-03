package kr.co.nogibackend.domain.notion.dto.content;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionParentResponse {

	private String type;
	private String database_id;
	private String page_id;

}
