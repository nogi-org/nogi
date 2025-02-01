package kr.co.nogibackend.interfaces.notion.response.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionDateProperty {

	private String start;
	private String end;
	private String time_zone;
}
