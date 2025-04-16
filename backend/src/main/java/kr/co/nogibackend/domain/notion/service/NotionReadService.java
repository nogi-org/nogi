package kr.co.nogibackend.domain.notion.service;

import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.config.context.ExecutionResultContext;
import kr.co.nogibackend.domain.notion.NotionClient;
import kr.co.nogibackend.domain.notion.dto.command.CompletedPageMarkdownCommand;
import kr.co.nogibackend.domain.notion.dto.info.NotionBaseInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockConversionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionGetAccessInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import kr.co.nogibackend.domain.notion.dto.result.CompletedPageMarkdownResult;
import kr.co.nogibackend.domain.notion.dto.result.NotionGetAccessResult;
import kr.co.nogibackend.domain.notion.helper.NotionDataProvider;
import kr.co.nogibackend.domain.notion.helper.NotionMarkdownConverter;
import kr.co.nogibackend.interfaces.notion.dto.request.NotionGetAccessTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
  노션 용어정리
  1. 데이터베이스: TIL 페이지를 담고있는 데이터베이스, 속성도 포함
  2. 페이지: 데이터베이스가 담고 있는 여러개의 페이지, 페이지는 각각 TIL 로 구분됨
  3. 블럭: 페이지에 작성된 내용, 한줄이 블럭 한개
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotionReadService {

  private final NotionClient notionClient;
  private final NotionMarkdownConverter notionMarkdownConverter;
  private final NotionDataProvider notionDataProvider;

  /**
   * <h2>📝 Notion에서 작성완료된 TIL 조회 및 Markdown 변환</h2>
   *
   * <ul>
   *   <li>1️⃣ 작성완료 상태 페이지 조회</li>
   *   <li>2️⃣ 각 페이지에 커밋 일자 및 시간 추가</li>
   *   <li>3️⃣ Notion 블록 정보 조회</li>
   *   <li>4️⃣ 블록 데이터를 Markdown 형식으로 변환</li>
   *   <li>5️⃣ 변환된 결과를 NotionStartTILResult 객체로 생성</li>
   *   <li>6️⃣ 모든 결과를 리스트에 담아 반환</li>
   * </ul>
   */
  public List<CompletedPageMarkdownResult> convertCompletedPageToMarkdown(
      CompletedPageMarkdownCommand command
  ) {
    // 작성완료 상태의 TIL 페이지 조회
    List<NotionPageInfo> pages =
        notionDataProvider
            .getCompletedPages(command.getNotionBotToken(), command.getNotionDatabaseId());

    List<CompletedPageMarkdownResult> results = new ArrayList<>();
    for (NotionPageInfo page : pages) {
      try {
        // 페이지에 커밋 일자 및 시간 추가
        page.getProperties().createCommitDateWithCurrentTime();

        // Notion 블록 정보 조회
        NotionBaseInfo<NotionBlockInfo> blocks =
            notionDataProvider
                .getBlocks(command.getNotionBotToken(), page.getId());

        // 블록 마크다운 변환 전처리
        notionDataProvider.preprocessMarkdown(blocks, command.getNotionBotToken());

        // 블록 마크다운 변환 처리
        NotionBlockConversionInfo markdown =
            notionMarkdownConverter.convert(blocks.getResults(), command.getGithubOwner());

        // 최종 데이터 결과 응답
        results.add(new CompletedPageMarkdownResult(command.getUserId(), page, markdown));
      } catch (Exception error) {
        // todo: 실패한 경우 page가 result에 안들어감. 아마도 알림에서 처리 못 할텐데...
        ExecutionResultContext
            .addNotionPageErrorResult(error.getMessage(), command.getUserId());
      }
    }

    // 변환된 결과 리스트 반환
    return results;
  }

  public NotionGetAccessResult getAccessToken(
      String basicToken
      , String code
      , String notionRedirectUrl
  ) {
    NotionGetAccessInfo notionInfo = notionClient.getAccessToken(
        basicToken,
        NotionGetAccessTokenRequest.of(code, notionRedirectUrl
        )
    );

    return new NotionGetAccessResult(
        notionInfo.accessToken(),
        notionInfo.duplicatedTemplateId()
    );
  }

  public String getNotionDatabaseInfo(
      String notionAccessToken
      , String notionPageId
  ) {
    NotionBaseInfo<NotionBlockInfo> notionPageInfo =
        notionClient.getBlocksFromParent(notionAccessToken, notionPageId, null);
    List<NotionBlockInfo> results = notionPageInfo.getResults();
    NotionBlockInfo childDatabase =
        results
            .stream()
            .filter(v -> v.getType().equals("child_database"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Notion Page 에서 Database 를 찾을 수 없습니다."));
    return childDatabase.getId();
  }

}
