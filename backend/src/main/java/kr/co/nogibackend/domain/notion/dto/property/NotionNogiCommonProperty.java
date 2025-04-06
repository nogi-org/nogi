package kr.co.nogibackend.domain.notion.dto.property;

import com.fasterxml.jackson.annotation.JsonInclude;
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

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String id = "sfsd_superpil";

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String type;

}
