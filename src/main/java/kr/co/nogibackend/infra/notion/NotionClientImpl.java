package kr.co.nogibackend.infra.notion;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import kr.co.nogibackend.domain.notion.NotionClient;
import kr.co.nogibackend.domain.notion.dto.info.DemoInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotionClientImpl implements NotionClient {

	private final NotionFeignClient notionFeignClient;

	@Override
	public ResponseEntity<List<DemoInfo>> getDemo() {
		return notionFeignClient.getDemo();
	}

	@Override
	public ResponseEntity<NotionInfo<NotionPageInfo>> getPagesFromDatabase(
		String authToken,
		String databaseId,
		Map<String, Object> request
	) {
		return notionFeignClient.getPagesFromDatabase(authToken, databaseId, request);
	}

	@Override
	public ResponseEntity<NotionInfo<NotionBlockInfo>> getBlocksFromPage(
		String authToken,
		String pageId
	) {
		return notionFeignClient.getBlocksFromPage(authToken, pageId);
	}
}
