package kr.co.nogibackend.domain.notion.service;

import java.util.List;
import kr.co.nogibackend.domain.notion.port.NotionClientPort;
import kr.co.nogibackend.domain.notion.result.NotionBaseResult;
import kr.co.nogibackend.domain.notion.result.NotionBlockResult;
import kr.co.nogibackend.domain.notion.result.NotionGetAccessResult;
import kr.co.nogibackend.domain.notion.result.NotionTokenPageIdResult;
import kr.co.nogibackend.interfaces.notion.request.NotionGetAccessTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotionAccountService {

	private final NotionClientPort notionClientPort;

	public NotionTokenPageIdResult getAccessToken(
			String basicToken
			, String code
			, String notionRedirectUrl
	) {
		NotionGetAccessResult notionInfo = notionClientPort.getAccessToken(
				basicToken,
				NotionGetAccessTokenRequest.of(code, notionRedirectUrl
				)
		);

		return new NotionTokenPageIdResult(
				notionInfo.accessToken(),
				notionInfo.duplicatedTemplateId()
		);
	}

	public String getNotionDatabaseInfo(
			String notionAccessToken
			, String notionPageId
	) {
		NotionBaseResult<NotionBlockResult> notionPageInfo =
				notionClientPort.getBlocksFromParent(notionAccessToken, notionPageId, null);
		List<NotionBlockResult> results = notionPageInfo.getResults();
		NotionBlockResult childDatabase =
				results
						.stream()
						.filter(v -> v.getType().equals("child_database"))
						.findFirst()
						.orElseThrow(() -> new RuntimeException("Notion Page 에서 Database 를 찾을 수 없습니다."));
		return childDatabase.getId();
	}

}
