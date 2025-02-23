package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotionDateProperty {

	private String start;
	private String end;
	private String time_zone;

	public NotionDateProperty(String start) {
		this.start = start;
	}

}
