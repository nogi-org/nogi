package kr.co.nogibackend.domain.notion.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/*
reference:
https://developers.notion.com/reference/property-object#date
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionDatabaseNogiProperties {

	private NotionNogiCommitDateProperty nogiCommitDate;
	private NotionDatabaseNogiTitleProperty nogiTitle;
	private NotionDatabaseNogiCommitMessageProperty nogiCommitMessage;
	private NotionDatabaseNogiCategoryProperty nogiCategory;
	private NotionDatabaseNogiStatusProperty nogiStatus;
	private NotionDatabaseNogiFrontMatterProperty nogiFrontMatter;

}
