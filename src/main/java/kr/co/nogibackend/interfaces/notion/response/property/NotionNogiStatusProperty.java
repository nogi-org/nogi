package kr.co.nogibackend.interfaces.notion.response.property;

import kr.co.nogibackend.interfaces.notion.response.content.NotionStatusResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionNogiStatusProperty extends NotionNogiProperty {

	private NotionStatusResponse select;
    
}
