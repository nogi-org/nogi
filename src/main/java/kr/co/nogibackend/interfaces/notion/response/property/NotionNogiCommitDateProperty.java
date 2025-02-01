package kr.co.nogibackend.interfaces.notion.response.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionNogiCommitDateProperty extends NotionNogiProperty {

	private NotionDateProperty date;

}
