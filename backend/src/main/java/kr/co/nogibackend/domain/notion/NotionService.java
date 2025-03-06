package kr.co.nogibackend.domain.notion;

import static kr.co.nogibackend.domain.notion.constant.NotionPropertyValue.STATUS_COMPLETED;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kr.co.nogibackend.config.context.ExecutionResultContext;
import kr.co.nogibackend.domain.notion.dto.command.NotionConnectionTestCommand;
import kr.co.nogibackend.domain.notion.dto.command.NotionEndTILCommand;
import kr.co.nogibackend.domain.notion.dto.command.NotionStartTILCommand;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockConversionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionGetAccessInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionPageInfo;
import kr.co.nogibackend.domain.notion.dto.request.NotionGetAccessTokenRequest;
import kr.co.nogibackend.domain.notion.dto.result.NotionEndTILResult;
import kr.co.nogibackend.domain.notion.dto.result.NotionGetAccessResult;
import kr.co.nogibackend.domain.notion.dto.result.NotionStartTILResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class NotionService {

  @Value("${github.resources-base-path}")
  public String RESOURCES_BASE_PATH;
  private final NotionClient notionClient;

  /**
   * <h2>📝 Notion에서 작성완료된 TIL 조회 및 Markdown 변환</h2>
   *
   * <ul>
   *   <li>1️⃣ 작성완료 상태의 TIL 페이지 조회</li>
   *   <li>2️⃣ 각 페이지에 커밋 일자 및 시간 추가</li>
   *   <li>3️⃣ Notion 블록 정보 조회</li>
   *   <li>4️⃣ 블록 데이터를 Markdown 형식으로 변환</li>
   *   <li>5️⃣ 변환된 결과를 NotionStartTILResult 객체로 생성</li>
   *   <li>6️⃣ 모든 결과를 리스트에 담아 반환</li>
   * </ul>
   */
  public List<NotionStartTILResult> startTIL(NotionStartTILCommand command) {
    // 1️⃣ 작성완료 상태의 TIL 페이지 조회
    List<NotionPageInfo> pages =
        this.getCompletedPages(command.getNotionBotToken(), command.getNotionDatabaseId());

    List<NotionStartTILResult> results = new ArrayList<>();
    for (NotionPageInfo page : pages) {
      // 2️⃣ 페이지에 커밋 일자 및 시간 추가
      page.getProperties().createCommitDateWithCurrentTime();

      // 3️⃣ Notion 블록 정보 조회
      NotionInfo<NotionBlockInfo> blocks =
          this.getBlocksOfPage(command.getNotionBotToken(), page, command.getUserId());

      // 4️⃣ 블록 데이터를 Markdown 형식으로 변환
      NotionBlockConversionInfo encodingOfBlock =
          this.convertMarkdown(blocks.getResults(), command.getUserId(), command.getGithubOwner());

      // 5️⃣ 변환된 결과를 NotionStartTILResult 객체로 생성
      results.add(new NotionStartTILResult(command.getUserId(), page, encodingOfBlock));
    }

    // 6️⃣ 변환된 결과 리스트 반환
    return results;
  }


  // Github 에 commit 된 결과를 notion 상태값 변경
  public List<NotionEndTILResult> endTIL(List<NotionEndTILCommand> commands) {
    return
        commands
            .stream()
            .map(this::endTIL)
            .flatMap(Optional::stream)
            .toList();
  }

  /**
   * <h2>✅ Notion TIL 상태 업데이트</h2>
   *
   * <ul>
   *   <li>1️⃣ TIL 커밋 성공/실패 여부를 기반으로 상태 업데이트</li>
   *   <li>2️⃣ Notion 페이지 ID, 사용자 ID를 이용해 상태 변경</li>
   *   <li>3️⃣ 성공한 경우 NotionEndTILResult 객체 생성</li>
   *   <li>4️⃣ 실패한 경우 Optional.empty() 반환</li>
   * </ul>
   */
  public Optional<NotionEndTILResult> endTIL(NotionEndTILCommand command) {
    // 1️⃣ TIL 커밋 성공/실패 여부를 기반으로 상태 업데이트
    boolean isUpdateResult =
        this.updateTILResultStatus(command.isSuccess(), command.notionBotToken(),
            command.notionPageId(),
            command.userId());

    // 2️⃣ Notion 페이지 ID, 사용자 ID를 이용해 상태 변경
    return
        isUpdateResult && command.isSuccess()
            ? Optional.of(
            // 3️⃣ 성공한 경우 NotionEndTILResult 객체 생성
            new NotionEndTILResult(
                command.userId(),
                command.notionPageId(),
                command.category(),
                command.title(),
                command.content()
            ))
            // 4️⃣ 실패한 경우 Optional.empty() 반환
            : Optional.empty();
  }


  // 노션 데이터베이스 연결 확인(단순 노션 데이터베이스에 페이지 조회 후 에러 없으면 성공처리)
  public void onConnectionTest(NotionConnectionTestCommand command) {
    notionClient.getDatabase(command.notionBotToken(), command.notionDatabaseId());
  }

  private boolean updateTILResultStatus(boolean isSuccess, String AuthToken, String pageId,
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

  // 노션 페이지의 블럭을 모두 불러오기(1회 최대 100개만 가져올 수 있음)
  private NotionInfo<NotionBlockInfo> getBlocksOfPage(
      String notionBotToken
      , NotionPageInfo page
      , Long userId
  ) {
    NotionInfo<NotionBlockInfo> blocks = this.getBlocks(notionBotToken, page.getId(), null, userId);

    // hasMore 이 true 면 next_cursor 로 다음 블럭을 가져온다.
    boolean hasMore = blocks.isHas_more();
    String nextCursor = blocks.getNext_cursor();

    while (hasMore) {
      NotionInfo<NotionBlockInfo> nextBlocks = this.getBlocks(notionBotToken, page.getId(),
          nextCursor, userId);
      blocks.getResults().addAll(nextBlocks.getResults());
      hasMore = nextBlocks.isHas_more();
      nextCursor = nextBlocks.getNext_cursor();
    }

    return blocks;
  }

  // 노션 페이지의 블럭 요청
  private NotionInfo<NotionBlockInfo> getBlocks(String AuthToken, String pageId, String startCursor,
      Long userId) {
    try {
      return notionClient.getBlocksFromPage(AuthToken, pageId, startCursor);
    } catch (Exception error) {
      ExecutionResultContext.addNotionPageErrorResult("Notion Block을 불러오는 중 문제가 발생했어요.", userId);
      return NotionInfo.empty();
    }
  }

  private List<NotionPageInfo> getCompletedPages(String AuthToken, String databaseId) {
    // todo: 페이지 가져올떄 한번에 100개 만 가져옴. 100개 이상이면 더 가져오는 로직 추가 필요
    try {
      Map<String, Object> filter = NotionRequestMaker.createPageFilterEqStatus(STATUS_COMPLETED);
      return
          notionClient
              .getPagesFromDatabase(AuthToken, databaseId, filter)
              .getResults();
    } catch (Exception error) {
      return List.of();
    }
  }

  /*
  블럭 조회 후 각각의 블럭을 markdown 으로 변환
  줄바꿈 경우: 띄어쓰기 두번 + \n 으로 처리
   */
  private NotionBlockConversionInfo convertMarkdown(
      List<NotionBlockInfo> blocks
      , Long userId
      , String githubOwner
  ) {
    StringBuilder markDown = new StringBuilder();
    List<NotionStartTILResult.ImageOfNotionBlock> images = new ArrayList<>();

    for (NotionBlockInfo block : blocks) {
      try {
        switch (block.getType()) {
          case "heading_1":
            markDown
                .append("# ")
                .append(
                    NotionRichTextContent.mergePlainText(block.getHeading_1().getRich_text(), true))
                .append("  \n");
            break;
          case "heading_2":
            markDown
                .append("## ")
                .append(
                    NotionRichTextContent.mergePlainText(block.getHeading_2().getRich_text(), true))
                .append("  \n");
            break;

          case "heading_3":
            markDown
                .append("### ")
                .append(
                    NotionRichTextContent.mergePlainText(block.getHeading_3().getRich_text(), true))
                .append("  \n");
            break;

          case "paragraph":
            if (block.getParagraph().getRich_text().isEmpty()) {
              markDown
                  .append("  \n");
            } else {
              markDown
                  .append(NotionRichTextContent.mergePlainText(block.getParagraph().getRich_text(),
                      true))
                  .append("  \n");
            }
            break;
          case "bulleted_list_item":
            markDown
                .append("* ")
                .append(
                    NotionRichTextContent.mergePlainText(
                        block.getBulleted_list_item().getRich_text(),
                        true))
                .append("  \n");
            break;

          case "numbered_list_item":
            markDown
                .append("1. ")
                .append(
                    NotionRichTextContent.mergePlainText(
                        block.getNumbered_list_item().getRich_text(), true)
                )
                .append("  \n");
            break;

          case "code":
            markDown
                .append("```")
                .append(block.getCode().getLanguage())
                .append("  \n");

            for (NotionRichTextContent richTest : block.getCode().getRich_text()) {
              markDown.append(richTest.getPlain_text()).append("  \n");
            }

            markDown
                .append("```")
                .append("  \n");
            break;

          case "divider":
            markDown.append("---").append("  \n");
            break;

          case "to_do":
            String checkBox = block.getTo_do().isChecked() ? "- [x] " : "- [ ] ";
            markDown
                .append(checkBox)
                .append(NotionRichTextContent.mergePlainText(block.getTo_do().getRich_text(), true))
                .append("  \n");
            break;

          case "image":
            // 이미지 요청
            URI imageUri = block.getImage().createURL();
            String imageEnc64 = this.getImageOfBlock(imageUri);

            // 이미지명 생성
            String fileName = block.getImage().createFileName();

            // 마크 다운에 들어갈 이미지 경로 생성
            String imagePath =
                block.getImage().createImagePath(RESOURCES_BASE_PATH, githubOwner, fileName);

            // 캡션 생성
            String caption = block.getImage().createCaption();

            markDown
                .append("![")
                .append(caption)
                .append("](")
                .append(imagePath)
                .append(")")
                .append("  \n");

            images
                .add(new NotionStartTILResult.ImageOfNotionBlock(imageEnc64, fileName, imagePath));

            break;

          default:
            markDown.append("  \n");
        }
      } catch (Exception error) {
        ExecutionResultContext.addNotionPageErrorResult("Markdown 변환 중 문제가 발생했어요.", userId);
        return new NotionBlockConversionInfo(markDown.toString(), images);
      }
    }
    return new NotionBlockConversionInfo(markDown.toString(), images);
  }

  private String getImageOfBlock(URI uri) {
    byte[] imageByte = notionClient.getBlockImage(uri);
    return Base64.getEncoder().encodeToString(imageByte);
  }

  public NotionGetAccessResult getAccessToken(String basicToken, String code,
      String notionRedirectUrl) {
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

  // TODO 코드 리팩터링
  public String getNotionDatabaseInfo(
      String notionAccessToken,
      String notionPageId
  ) {
    NotionInfo<NotionBlockInfo> notionPageInfo = notionClient.getBlocksFromPage(notionAccessToken,
        notionPageId, null
    );
    List<NotionBlockInfo> results = notionPageInfo.getResults();
    NotionBlockInfo childDatabase = results.stream()
        .filter(v -> v.getType().equals("child_database")).findFirst()
        .orElseThrow(() -> new RuntimeException("Notion Page 에서 Database 를 찾을 수 없습니다."));
    return childDatabase.getId();
  }
}
