package kr.co.nogibackend.interfaces.notion.response;

import java.util.List;

public record NotionDatabasePropertyCreateResponse(
		List<Long> successIds,
		List<Long> failIds
) {

}
