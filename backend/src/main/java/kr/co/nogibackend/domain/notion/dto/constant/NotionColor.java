package kr.co.nogibackend.domain.notion.dto.constant;


import lombok.Getter;

@Getter
public enum NotionColor {

	RED("red"),
	BLUE("blue");

	private final String name;

	NotionColor(String name) {
		this.name = name;
	}

}

