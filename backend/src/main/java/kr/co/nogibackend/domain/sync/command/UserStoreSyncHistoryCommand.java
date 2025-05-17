package kr.co.nogibackend.domain.sync.command;

import kr.co.nogibackend.domain.notion.result.NotionEndTILResult;

public record UserStoreSyncHistoryCommand(
		Long userId, // 유저 ID
		String notionPageId, // 노션 페이지 ID
		String category,
		String title,
		String content
) {

	public static UserStoreSyncHistoryCommand from(
			NotionEndTILResult notionEndTILResult
	) {
		return new UserStoreSyncHistoryCommand(
				notionEndTILResult.userId(),
				notionEndTILResult.notionPageId(),
				notionEndTILResult.category(),
				notionEndTILResult.title(),
				notionEndTILResult.content()
		);
	}
}
