package kr.co.nogibackend.domain.notion.dto.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotionDateProperty {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String start;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String end;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String time_zone;

	public NotionDateProperty(String start) {
		this.start = start;
	}

}
