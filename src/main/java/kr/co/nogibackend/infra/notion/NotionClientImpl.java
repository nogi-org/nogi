package kr.co.nogibackend.infra.notion;

import java.net.URI;
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
			// todo: 익셉션 떨궈서 사용하는 쪽에서 처리하게 하기
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
			// todo: 익셉션 떨궈서 사용하는 쪽에서 처리하게 하기
			System.out.println(error.getMessage());
			return NotionInfo.empty();
		}
	}

	@Override
	public byte[] getBlockImage(URI baseUri) {
		// todo: 익셉션 떨궈서 사용하는 쪽에서 처리하게 하기
		return notionImageFeignClient.getBlockImage(baseUri);
	}

}
