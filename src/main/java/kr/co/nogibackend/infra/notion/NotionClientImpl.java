package kr.co.nogibackend.infra.notion;

import static kr.co.nogibackend.response.code.NotionResponseCode.*;

import java.net.URI;
import java.util.Map;

import org.springframework.stereotype.Component;

import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.notion.NotionClient;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotionClientImpl implements NotionClient {

	private final NotionFeignClient notionFeignClient;
	private final NotionImageFeignClient notionImageFeignClient;

	@Override
	public NotionInfo<NotionPageInfo> getPagesFromDatabase(
		String authToken,
		String databaseId,
		Map<String, Object> request
	) {
		try {
			return
				notionFeignClient
					.getPagesFromDatabase(authToken, databaseId, request)
					.getBody();
		} catch (Exception error) {
			throw new GlobalException(F_GET_NOTION_PAGE);
		}
	}

	@Override
	public NotionInfo<NotionBlockInfo> getBlocksFromPage(
		String authToken,
		String pageId,
		String startCursor
	) {
		try {
			return
				notionFeignClient
					.getBlocksFromPage(authToken, pageId, startCursor)
					.getBody();
		} catch (Exception error) {
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
	public NotionPageInfo updatePageStatus(
		String authToken
		, String pageId
		, Map<String, Object> request
	) {
		try {
			return
				notionFeignClient
					.updatePageStatus(authToken, pageId, request)
					.getBody();
		} catch (Exception error) {
			throw new GlobalException(F_UPDATE_TIL_STATUS);
		}

	}

}
