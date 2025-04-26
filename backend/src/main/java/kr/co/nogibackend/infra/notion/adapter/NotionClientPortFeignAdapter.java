package kr.co.nogibackend.infra.notion.adapter;

import static kr.co.nogibackend.global.response.code.NotionResponseCode.F_GET_BLOCK_IMAGE;
import static kr.co.nogibackend.global.response.code.NotionResponseCode.F_GET_NOTION_BLOCK;
import static kr.co.nogibackend.global.response.code.NotionResponseCode.F_GET_NOTION_DATABASE;
import static kr.co.nogibackend.global.response.code.NotionResponseCode.F_GET_NOTION_PAGE;
import static kr.co.nogibackend.global.response.code.NotionResponseCode.F_UPDATE_TIL_STATUS;

import java.net.URI;
import java.util.Map;
import kr.co.nogibackend.domain.notion.command.NotionCreateNoticeCommand;
import kr.co.nogibackend.domain.notion.port.NotionClientPort;
import kr.co.nogibackend.domain.notion.result.NotionBaseResult;
import kr.co.nogibackend.domain.notion.result.NotionBlockResult;
import kr.co.nogibackend.domain.notion.result.NotionDatabaseResult;
import kr.co.nogibackend.domain.notion.result.NotionGetAccessResult;
import kr.co.nogibackend.domain.notion.result.NotionPageResult;
import kr.co.nogibackend.global.config.exception.GlobalException;
import kr.co.nogibackend.infra.notion.client.NotionFeignClient;
import kr.co.nogibackend.infra.notion.client.NotionImageFeignClient;
import kr.co.nogibackend.interfaces.notion.request.NotionGetAccessTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotionClientPortFeignAdapter implements NotionClientPort {

	private final NotionFeignClient notionFeignClient;
	private final NotionImageFeignClient notionImageFeignClient;

	@Override
	public NotionBaseResult<NotionPageResult> getPagesFromDatabase(
			String BotToken,
			String databaseId,
			Map<String, Object> request
	) {
		try {
			return
					notionFeignClient
							.getPagesFromDatabase(BotToken, databaseId, request)
							.getBody();
		} catch (Exception error) {
			log.error("Notion Get Page From Database API Fail Message : {}", error.getMessage());
			throw new GlobalException(F_GET_NOTION_PAGE);
		}
	}

	@Override
	public NotionBaseResult<NotionBlockResult> getBlocksFromParent(
			String accessToken
			, String parentBlockId
			, String startCursor
	) {
		return this.callBlocksFromParentClient(accessToken, parentBlockId, startCursor);
	}

	@Override
	public NotionBaseResult<NotionBlockResult> getBlocksFromParent(
			String accessToken,
			String parentBlockId
	) {
		return this.callBlocksFromParentClient(accessToken, parentBlockId, null);
	}

	private NotionBaseResult<NotionBlockResult> callBlocksFromParentClient(
			String accessToken
			, String parentBlockId
			, String startCursor
	) {
		try {
			return
					notionFeignClient
							.getBlocksFromParent(accessToken, parentBlockId, startCursor)
							.getBody();
		} catch (Exception error) {
			log.error("Notion Get Blocks API Fail Message: {}", error.getMessage());
			throw new GlobalException(F_GET_NOTION_BLOCK);
		}
	}

	@Override
	public byte[] getBlockImage(URI baseUri) {
		try {
			return notionImageFeignClient.getBlockImage(baseUri);
		} catch (Exception error) {
			throw new GlobalException(F_GET_BLOCK_IMAGE);
		}
	}

	@Override
	public NotionPageResult updatePageStatus(
			String BotToken
			, String pageId
			, Map<String, Object> request
	) {
		try {
			return
					notionFeignClient
							.updatePageStatus(BotToken, pageId, request)
							.getBody();
		} catch (Exception error) {
			throw new GlobalException(F_UPDATE_TIL_STATUS);
		}

	}

	@Override
	public NotionDatabaseResult getDatabase(
			String BotToken
			, String databaseId
	) {
		try {
			return
					notionFeignClient.getDatabase(BotToken, databaseId).getBody();
		} catch (Exception error) {
			throw new GlobalException(F_GET_NOTION_DATABASE);
		}
	}

	@Override
	public NotionGetAccessResult getAccessToken(
			String basicToken,
			NotionGetAccessTokenRequest request
	) {
		return notionFeignClient.getAccessToken(basicToken, request).getBody();
	}

	@Override
	public NotionBaseResult<NotionPageResult> createPage(
			String basicToken,
			NotionCreateNoticeCommand request
	) {
		return notionFeignClient.createPage(basicToken, request).getBody();
	}

}
