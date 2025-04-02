package kr.co.nogibackend.domain.notion.dto.property;

import kr.co.nogibackend.domain.notion.dto.constant.NotionColor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionNogiStatusProperty extends NotionNogiCommonProperty {

	private NotionStatusProperty select;

	public static NotionNogiStatusProperty buildColorStatus(
			String name
			, NotionColor color
	) {
		return
				NotionNogiStatusProperty
						.builder()
						.select(NotionStatusProperty.buildColorName(name, color.getName()))
						.build();
	}

}
