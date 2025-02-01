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
public class NotionNogiProperties {

	private NotionNogiCommitDateProperty nogiCommitDate;
	private NotionNogiStatusProperty nogiStatus;
	private NotionNogiCategoryProperty nogiCategory;
	private NotionNogiTitleProperty nogiTitle;

}
