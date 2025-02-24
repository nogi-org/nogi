package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
reference:
https://developers.notion.com/reference/property-object#date
 */
@Getter
@Setter
@ToString
public class NotionNogiCommonProperty {

	private String id;
	private String type;

}
