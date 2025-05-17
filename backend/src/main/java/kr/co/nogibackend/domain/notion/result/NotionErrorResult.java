package kr.co.nogibackend.domain.notion.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionErrorResult {

	private String object;
	private int status;
	private String code;
	private String message;

}
