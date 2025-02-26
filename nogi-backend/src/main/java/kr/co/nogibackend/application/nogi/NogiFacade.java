package kr.co.nogibackend.application.nogi;

import static kr.co.nogibackend.response.code.UserResponseCode.F_MANUAL;

import java.util.List;
import kr.co.nogibackend.config.context.ExecutionResultContext;
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
import org.springframework.stereotype.Service;

/*
  Package Name : kr.co.nogibackend.application.notion
  File Name    : NotionFacade
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
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

  private void onNogi(UserResult user) {
    if (user.isUnProcessableToNogi()) {
      return;
    }

    try {
      // notion TIL 페이지 조회 후 markdown 변환
      List<NotionStartTILResult> notionStartTILResults =
          notionService.startTIL(NotionStartTILCommand.from(user));

      // TIL 생성 또는 수정 체크
      List<UserCheckTILCommand> userCheckTILCommands =
          notionStartTILResults.stream().map(UserCheckTILCommand::from).toList();
      List<UserCheckTILResult> userCheckTILResults = userService.checkTIL(userCheckTILCommands);

      // 마크다운을 유저 레파지토리에 commit 하기
      List<GithubCommitCommand> githubCommitCommands =
          GithubCommitCommand.of(notionStartTILResults, userCheckTILResults);
      List<GithubCommitResult> githubCommitResults = githubService.commitToGithub(
          githubCommitCommands);

      // commit 성공과 실패를 notion 상태값 변경하기
      List<NotionEndTILCommand> notionEndTILCommands =
          githubCommitResults.stream().map(NotionEndTILCommand::from).toList();
      List<NotionEndTILResult> notionEndTILResults = notionService.endTIL(notionEndTILCommands);

      // NogiHistory 에 저장 또는 수정 && 성공 결과를 ExecutionResultContext 에 저장
      List<UserStoreNogiHistoryCommand> userStoreNogiHistoryCommands =
          notionEndTILResults.stream().map(UserStoreNogiHistoryCommand::from).toList();
      userService.storeNogiHistory(userStoreNogiHistoryCommands);

      // user 가 알림을 동의 했을 경우 github issue 를 통해 유저에게 알림 전송
      if (user.isNotificationAllowed()) {
        List<UserResult> userResult = userService.getUsersByIds(
            ExecutionResultContext.getResults()
                .stream()
                .map(ExecutionResultContext.ProcessingResult::userId)
                .distinct()
                .toArray(Long[]::new)
        );
        userService.findNogiBot().ifPresent((masterUser) -> {
          githubService.notify(GithubNotifyCommand.from(userResult, masterUser));
        });
      }
    } finally {
      ExecutionResultContext.clear();
    }
  }

}
