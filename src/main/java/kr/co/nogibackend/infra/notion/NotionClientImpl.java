package kr.co.nogibackend.infra.notion;

import java.util.Map;

import org.springframework.stereotype.Component;

import kr.co.nogibackend.domain.notion.NotionClient;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotionClientImpl implements NotionClient {

	private final NotionFeignClient notionFeignClient;

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
			// todo: 에러 핸들링 하기(notice 역할 만들고 작업하기)
			System.out.println(error.getMessage());
			return NotionInfo.empty();
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
			// todo: 에러 핸들링 하기(notice 역할 만들고 작업하기)
			System.out.println(error.getMessage());
			return NotionInfo.empty();
		}
	}

}
