package kr.co.nogibackend.domain.notion.dto.property;

import kr.co.nogibackend.domain.notion.dto.content.NotionStatusResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionNogiStatusProperty extends NotionNogiProperty {

	private NotionStatusResponse select;

}
