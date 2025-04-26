package kr.co.nogibackend.domain.notion.port;

import java.net.URI;
import java.util.Map;
import kr.co.nogibackend.domain.notion.command.NotionCreateNoticeCommand;
import kr.co.nogibackend.domain.notion.result.NotionBaseResult;
import kr.co.nogibackend.domain.notion.result.NotionBlockResult;
import kr.co.nogibackend.domain.notion.result.NotionDatabaseResult;
import kr.co.nogibackend.domain.notion.result.NotionGetAccessResult;
import kr.co.nogibackend.domain.notion.result.NotionPageResult;
import kr.co.nogibackend.interfaces.notion.request.NotionGetAccessTokenRequest;

public interface NotionClientPort {

	NotionBaseResult<NotionPageResult> getPagesFromDatabase
			(String BotToken, String databaseId, Map<String, Object> request);

	NotionBaseResult<NotionBlockResult> getBlocksFromParent
			(String BotToken, String pageId, String startCursor);

	NotionBaseResult<NotionBlockResult> getBlocksFromParent
			(String BotToken, String pageId);

	byte[] getBlockImage(URI baseUri);

	NotionPageResult updatePageStatus
			(String BotToken, String pageId, Map<String, Object> request);

	NotionDatabaseResult getDatabase(String BotToken, String databaseId);

	NotionGetAccessResult getAccessToken(String basicToken, NotionGetAccessTokenRequest request);

	NotionBaseResult<NotionPageResult> createPage
			(String basicToken, NotionCreateNoticeCommand request);

}
