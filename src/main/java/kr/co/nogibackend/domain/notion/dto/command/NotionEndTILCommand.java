package kr.co.nogibackend.domain.notion.dto.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotionEndTILCommand {

	private String notionAuthToken; // notion auth token
	private String notionPageId; // notion page id
	private boolean isSuccess; // 성공여부

}
