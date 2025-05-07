package kr.co.nogibackend.domain.notion.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionDatabaseNogiStatusProperty extends NotionNogiCommonProperty {

	private NotionStatusOptionsProperty select;

}
