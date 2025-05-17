package kr.co.nogibackend.domain.sync.constant;

import lombok.Getter;

@Getter
public enum SyncHistoryType {

	CREATE_OR_UPDATE_CONTENT("생성 또는 내용 수정"),
	UPDATE_TITLE("제목 수정"),
	UPDATE_CATEGORY("카테고리 수정");

	private final String name;

	SyncHistoryType(String name) {
		this.name = name;
	}

}
