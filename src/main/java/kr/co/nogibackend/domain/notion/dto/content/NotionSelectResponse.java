package kr.co.nogibackend.domain.notion.dto.content;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionSelectResponse {

	private String id;
	private String name;
	private String color;

}
