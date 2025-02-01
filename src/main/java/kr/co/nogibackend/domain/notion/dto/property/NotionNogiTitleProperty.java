package kr.co.nogibackend.domain.notion.dto.property;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionNogiTitleProperty extends NotionNogiProperty {

	private List<NotionTitleProperty> title;

}
