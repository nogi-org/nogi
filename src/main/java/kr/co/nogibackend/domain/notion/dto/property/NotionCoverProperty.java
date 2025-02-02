package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionCoverProperty {

	private String type;
	private NotionFileProperty external;

}
