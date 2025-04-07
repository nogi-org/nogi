package kr.co.nogibackend.domain.notion.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kr.co.nogibackend.config.context.ExecutionResultContext;
import kr.co.nogibackend.domain.admin.dto.request.NotionCreateNoticeRequest;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notion.NotionClient;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.result.PublishNewNoticeResult;
import kr.co.nogibackend.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotionDataInjector {

  private final NotionClient notionClient;

  public boolean updateTILResultStatus(
      boolean isSuccess
      , String AuthToken
      , String pageId
      , Long userId
  ) {
    try {
      Map<String, Object> request = NotionRequestMaker.requestUpdateStatusOfPage(isSuccess);
      notionClient.updatePageStatus(AuthToken, pageId, request);
      return true;
    } catch (Exception error) {
      ExecutionResultContext.addNotionPageErrorResult("TIL 결과를 Notion에 반영 중 문제가 발생했어요.", userId);
      return false;
    }
  }

  /**
   * <h1>Notion 공지사항 발행</h1>
   */
  public List<PublishNewNoticeResult> publishNewNotice(List<User> users, Notice notice) {
    List<PublishNewNoticeResult> results = new ArrayList<>();
    List<NotionBlockInfo> content = notice.buildContentCallOutBlock();

    for (User user : users) {
      boolean isSuccess = true;

      try {
        user.validateHasNotionTokenOrDatabaseId();

        NotionCreateNoticeRequest request =
            NotionCreateNoticeRequest.ofNotice(
                user.getNotionDatabaseId()
                , notice.getTitle()
                , content
            );

        notionClient.createPage(user.getNotionAccessToken(), request);
      } catch (Exception error) {
        isSuccess = false;
      }

      results.add(new PublishNewNoticeResult(user, isSuccess));
    }

    return results;
  }

}
