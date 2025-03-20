package kr.co.nogibackend.domain.notion.dto.constant;

import lombok.Getter;

@Getter
public enum NotionPropertyValue {

	STATUS_DRAFTING("작성중"),
	STATUS_COMPLETED("작성완료"),
	STATUS_FAILED("실패"),
	STATUS_DONE("완료");

	private final String name;

	NotionPropertyValue(String name) {
		this.name = name;
	}

}
