package kr.co.nogibackend.application.nogi;

import static kr.co.nogibackend.response.code.UserResponseCode.F_MANUAL;
import static kr.co.nogibackend.response.code.UserResponseCode.F_NOT_FOUND_NOGI_BOT;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.co.nogibackend.config.context.ExecutionResultContext;
import kr.co.nogibackend.config.context.ExecutionResultContext.ProcessingResult;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.github.GithubService;
import kr.co.nogibackend.domain.github.dto.command.GithubCommitCommand;
import kr.co.nogibackend.domain.github.dto.command.GithubNotifyCommand;
import kr.co.nogibackend.domain.github.dto.result.GithubCommitResult;
import kr.co.nogibackend.domain.notion.NotionService;
import kr.co.nogibackend.domain.notion.dto.command.NotionEndTILCommand;
import kr.co.nogibackend.domain.notion.dto.command.NotionStartTILCommand;
import kr.co.nogibackend.domain.notion.dto.result.NotionEndTILResult;
import kr.co.nogibackend.domain.notion.dto.result.NotionStartTILResult;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.domain.user.dto.command.UserCheckTILCommand;
import kr.co.nogibackend.domain.user.dto.command.UserStoreNogiHistoryCommand;
import kr.co.nogibackend.domain.user.dto.result.UserCheckTILResult;
import kr.co.nogibackend.domain.user.dto.result.UserResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NogiFacade {

  private final NotionService notionService;
  private final UserService userService;
  private final GithubService githubService;

  // 자동 실행
  public void onAuto() {
    userService.findUser().forEach(this::onNogi);
  }

  // 수동 실행
  public void onManual(Long userId) {
    this.onNogi(userService.findUserByIdForFacade(userId));
    List<ExecutionResultContext.ProcessingResult> errorResult =
        ExecutionResultContext
            .getResults()
            .stream()
            .filter(item -> !item.success())
            .toList();

    if (!errorResult.isEmpty()) {
      StringBuilder errorMessage = new StringBuilder();
      ExecutionResultContext.getResults()
          .forEach(item -> errorMessage.append(item.message()).append("\n"));
      throw new GlobalException(F_MANUAL, errorMessage.toString());
    }
  }

  /**
   * <h2>🚀 Nogi 프로세스 실행</h2>
   *
   * <ul>
   *   <li>1️⃣ 유저가 Nogi 처리가 가능한지 확인</li>
   *   <li>2️⃣ Notion에서 TIL 데이터를 가져와 Markdown으로 변환</li>
   *   <li>3️⃣ TIL이 생성되었거나 수정이 필요한지 체크</li>
   *   <li>4️⃣ Markdown을 GitHub 레포지토리에 커밋</li>
   *   <li>5️⃣ 커밋 성공/실패 여부를 기반으로 Notion 상태값 변경</li>
   *   <li>6️⃣ Nogi 히스토리를 저장 또는 수정</li>
   *   <li>7️⃣ 📢 유저가 알림을 동의한 경우 GitHub Issue를 통해 알림 전송</li>
   *   <li>8️⃣ 🔄 ExecutionResultContext 정리</li>
   * </ul>
   */
  private void onNogi(UserResult user) {
    // TODO notion_page_id 값이 있으면서 notion_database_id 가 없는 경우 notion api 요청하여 user 정보 업데이트

    // 1️⃣ 처리 불가능한 경우 바로 종료
    if (user.isUnProcessableToNogi()) {
      return;
    }

    try {
      // 2️⃣ Notion TIL 페이지 조회 후 Markdown 변환 📝
      List<NotionStartTILResult> notionStartTILResults =
          notionService.startTIL(NotionStartTILCommand.from(user));
      logStartTilResults(notionStartTILResults);

      // 3️⃣ TIL 생성 또는 수정 체크 🔍
      List<UserCheckTILCommand> userCheckTILCommands =
          notionStartTILResults.stream().map(UserCheckTILCommand::from).toList();
      List<UserCheckTILResult> userCheckTILResults = userService.checkTIL(userCheckTILCommands);

      // 4️⃣ Markdown을 GitHub에 커밋 🚀
      UserResult nogiBotResult = userService.findNogiBot()
          .orElseThrow(() -> new GlobalException(F_NOT_FOUND_NOGI_BOT));
      List<GithubCommitCommand> githubCommitCommands =
          GithubCommitCommand.of(notionStartTILResults, userCheckTILResults, nogiBotResult);
      List<GithubCommitResult> githubCommitResults = githubService.commitToGithub(
          githubCommitCommands);

      // 5️⃣ 커밋 성공/실패를 Notion 상태값 변경 📌
      List<NotionEndTILCommand> notionEndTILCommands =
          githubCommitResults.stream().map(NotionEndTILCommand::from).toList();
      List<NotionEndTILResult> notionEndTILResults = notionService.endTIL(notionEndTILCommands);

      // 6️⃣ NogiHistory 저장 또는 수정 🏷️
      List<UserStoreNogiHistoryCommand> userStoreNogiHistoryCommands =
          notionEndTILResults.stream().map(UserStoreNogiHistoryCommand::from).toList();
      userService.storeNogiHistory(userStoreNogiHistoryCommands);

      // 7️⃣ 📢 유저 알림 전송 (GitHub Issue 활용)
      if (user.isNotificationAllowed()) {
        List<UserResult> userResult = userService.getUsersByIds(
            ExecutionResultContext.getResults()
                .stream()
                .map(ExecutionResultContext.ProcessingResult::userId)
                .distinct()
                .toArray(Long[]::new)
        );
        githubService.notify(GithubNotifyCommand.from(userResult, nogiBotResult));
      }
    } finally {
      logFailureResults();

      // 8️⃣ ExecutionResultContext 정리 🧹
      ExecutionResultContext.clear();
    }
  }


  private void logStartTilResults(List<NotionStartTILResult> notionStartTILResults) {
    if (notionStartTILResults.isEmpty()) {
      return;
    }

    log.info("After Notion StartTIL:\n{}",
        notionStartTILResults.stream()
            .map(result -> String.format(
                " - userId: %d, category: %s, title: %s, notionPageId: %s",
                result.userId(), result.category(), result.title(), result.notionPageId()))
            .collect(Collectors.joining("\n")));
  }

  private void logFailureResults() {
    List<ProcessingResult> failureResult = Optional.ofNullable(
            ExecutionResultContext.getResults())
        .orElse(Collections.emptyList())
        .stream()
        .filter(v -> !v.success())
        .toList();

    if (!failureResult.isEmpty()) {
      log.error("Nogi 처리 중 오류가 발생했습니다. 오류 내용:\n{}",
          failureResult.stream()
              .map(result -> String.format(" - %s", result))
              .collect(Collectors.joining("\n"))
      );
    }
  }


}
