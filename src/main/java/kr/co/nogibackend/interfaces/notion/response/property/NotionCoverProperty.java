package kr.co.nogibackend.interfaces.notion.response.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionCoverProperty {

	private String type;
	private NotionFileProperty external;

}
