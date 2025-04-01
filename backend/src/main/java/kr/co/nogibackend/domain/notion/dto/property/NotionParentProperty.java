package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotionParentProperty {

	private String type;
	private String database_id;
	private String page_id;
	private String block_id;

	public static NotionParentProperty buildParent(
			PARENT_TYPE type
			, String id
	) {
		NotionParentPropertyBuilder property = NotionParentProperty.builder();
		if (type.equals(PARENT_TYPE.DATABASE)) {
			property.type(type.value).database_id(id);
		} else if (type.equals(PARENT_TYPE.PAGE)) {
			property.type(type.value).page_id(id);
		} else if (type.equals(PARENT_TYPE.BLOCK)) {
			property.type(type.value).page_id(id);
		}
		return property.build();
	}

	public enum PARENT_TYPE {
		DATABASE("database"), PAGE("page"), BLOCK("block");

		private final String value;

		PARENT_TYPE(String value) {
			this.value = value;
		}
	}

}
