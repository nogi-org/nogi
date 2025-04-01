package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionMultiSelectProperty {

	private String id;
	private String name;
	private String color;

	public static NotionMultiSelectProperty buildColorName(String name, String color) {
		return
				NotionMultiSelectProperty
						.builder()
						.name(name)
						.color(color)
						.build();
	}

}
