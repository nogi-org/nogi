package kr.co.nogibackend.domain.user;

import lombok.Getter;

@Getter
public enum NogiHistoryType {

	CREATE("생성"),
	UPDATE_CONTENT("내용 수정"),
	UPDATE_TITLE_OR_CATEGORY("제목 또는 카테고리 수정");

	private final String name;

	NogiHistoryType(String name) {
		this.name = name;
	}

}
