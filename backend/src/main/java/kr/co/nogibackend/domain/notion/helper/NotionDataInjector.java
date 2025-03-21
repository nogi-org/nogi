package kr.co.nogibackend.domain.notion.helper;

import java.util.Map;
import kr.co.nogibackend.config.context.ExecutionResultContext;
import kr.co.nogibackend.domain.notion.NotionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotionDataInjector {

  private final NotionClient notionClient;

  public boolean updateTILResultStatus(boolean isSuccess, String AuthToken, String pageId,
      Long userId) {
    try {
      Map<String, Object> request = NotionRequestMaker.requestUpdateStatusOfPage(isSuccess);
      notionClient.updatePageStatus(AuthToken, pageId, request);
      return true;
    } catch (Exception error) {
      ExecutionResultContext.addNotionPageErrorResult("TIL 결과를 Notion에 반영 중 문제가 발생했어요.", userId);
      return false;
    }
  }

}
