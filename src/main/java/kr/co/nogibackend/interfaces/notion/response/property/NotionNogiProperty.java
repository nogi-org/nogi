package kr.co.nogibackend.interfaces.notion.response.property;

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
public class NotionNogiProperty {

	private String id;
	private String type;

}
