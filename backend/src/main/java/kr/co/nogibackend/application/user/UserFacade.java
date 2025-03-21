package kr.co.nogibackend.application.user;

import static kr.co.nogibackend.response.code.GitResponseCode.F_ALREADY_USING_REPOSITORY_NAME;
import static kr.co.nogibackend.response.code.UserResponseCode.F_NOT_FOUND_USER;

import java.util.List;
import kr.co.nogibackend.application.notion.dto.NotionLoginEventCommand;
import kr.co.nogibackend.application.user.dto.UserFacadeCommand;
import kr.co.nogibackend.config.audit.AuditContext;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.config.security.JwtProvider;
import kr.co.nogibackend.domain.github.GithubClient;
import kr.co.nogibackend.domain.github.GithubService;
import kr.co.nogibackend.domain.github.dto.command.GithubAddCollaboratorCommand;
import kr.co.nogibackend.domain.github.dto.command.GithubGetRepositoryCommand;
import kr.co.nogibackend.domain.github.dto.info.GithubRepoInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserEmailInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubValidateInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubOAuthAccessTokenRequest;
import kr.co.nogibackend.domain.github.dto.result.GithubUserResult;
import kr.co.nogibackend.domain.notion.dto.result.NotionGetAccessResult;
import kr.co.nogibackend.domain.notion.service.NotionReadService;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.domain.user.dto.command.UserUpdateCommand;
import kr.co.nogibackend.domain.user.dto.info.UserInfo;
import kr.co.nogibackend.domain.user.dto.info.UserLoginByGithubInfo;
import kr.co.nogibackend.domain.user.dto.info.UserLoginByNotionInfo;
import kr.co.nogibackend.domain.user.dto.result.UserResult;
import kr.co.nogibackend.domain.user.dto.result.UserSinUpOrUpdateResult;
import kr.co.nogibackend.response.code.GitResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final UserService userService;
  private final GithubService githubService;
  private final GithubClient githubClient;
  private final NotionReadService notionReadService;
  private final JwtProvider jwtProvider;
  private final ApplicationEventPublisher applicationEventPublisher;

  public UserLoginByGithubInfo loginByGithub(UserFacadeCommand.GithubLogin command) {
    try {
      // 1. access token 가져오기
      String githubAccessToken = githubService.getAccessToken(
          new GithubOAuthAccessTokenRequest(
              command.clientId(),
              command.clientSecret(),
              command.code(),
              "public_repo"
          )
      ).accessToken();

      // 2. user 정보 가져오기
      GithubUserResult githubUserResult = githubService.getUserInfo(githubAccessToken);

      // 3. AuditContext 에 저장(DB auditing 을 위함)
      AuditContext.setUserId(0L);

      // 4. user 정보 저장하기
      UserSinUpOrUpdateResult sinUpOrUpdateResult =
          userService.signUpOrUpdateUser(
              githubUserResult.id(),
              UserUpdateCommand.fromGithubLogin(githubUserResult, githubAccessToken)
          );

      // 5. access toekn 발급하기(nogi token)
      String nogiAccessToken = jwtProvider.generateToken(
          sinUpOrUpdateResult.id(),
          sinUpOrUpdateResult.role()
      );

      // 6. 신규유저일 경우 알림보내개(경축!)
      if (sinUpOrUpdateResult.isSinUp()) {
        userService.sendSinUpNotification(
            sinUpOrUpdateResult.id(),
            sinUpOrUpdateResult.githubOwner()
        );
      }

      return UserLoginByGithubInfo.from(
          sinUpOrUpdateResult,
          nogiAccessToken,
          true,
          "Github 로그인을 성공하였어요!"
      );
    } catch (Exception e) {
      return UserLoginByGithubInfo.from(false, "Github 로그인에 실패했어요.");
    }
  }

  public UserLoginByNotionInfo loginByNotion(UserFacadeCommand.NotionLogin command) {
    try {
      // 1. access token 가져오기
      NotionGetAccessResult notionResult = notionReadService.getAccessToken(
          command.getBasicToken(),
          command.code(),
          command.notionRedirectUri()
      );
      String notionPageId = notionResult.notionPageId();
      // 1-1. notion page id 가 없는 경우 -> 유저가 개발자가 제공한 템플릿을 선택하지 않은 경우
      if (!StringUtils.hasText(notionPageId)) {
        String message = "개발자가 제공한 템플릿을 선택해야해주세요!";
        return new UserLoginByNotionInfo(false, message);
      }

      // 2. database 정보 가져와서 user 정보 업데이트(notionDatabaseId 저장)
      applicationEventPublisher.publishEvent(
          new NotionLoginEventCommand(
              command.userId(),
              notionResult.notionAccessToken(),
              notionResult.notionPageId()
          )
      );

      // 3. AuditContext 에 저장(DB auditing 을 위함) -> callbackUrl 로 동작하는 메서드라 따로 처리
      AuditContext.setUserId(command.userId());

      // 4. user 정보 업데이트(notionPageId 저장)
      userService.updateUser(
          UserUpdateCommand.builder()
              .id(command.userId())
              .notionAccessToken(notionResult.notionAccessToken())
              .notionPageId(notionResult.notionPageId())
              .build()
      );

      return new UserLoginByNotionInfo(true, "Notion 과 성공적으로 연결하였어요!");
    } catch (Exception e) {
      return new UserLoginByNotionInfo(false, "Notion 과 연결하는데 실패했어요.");
    }
  }


  public UserInfo updateUserAndCreateRepo(UserUpdateCommand command) {
    // 1. user 정보 가져오기
    UserResult userResult = userService.findUserByIdForFacade(command.getId());

    // 2. github 에 실제로 repo 가 존재하는지 확인하고 없으면 생성
    handleRepositoryCreationOrUpdate(command, userResult);

    // 3. DB 에 user 정보 업데이트
    return userService.updateUser(command);
  }

  private void handleRepositoryCreationOrUpdate(UserUpdateCommand command, UserResult userResult) {
    String newRepo = command.getGithubRepository();
    if (!StringUtils.hasText(newRepo)) {
      return;
    }

    boolean isDeletedOnGithub = isRepositoryDeletedOnGithub(
        userResult.githubOwner(),
        newRepo,
        userResult.githubAuthToken()
    );

    // GitHub 에서 삭제된 상태일 때 새롭게 생성
    if (isDeletedOnGithub) {
      String defaultBranch = createRepositoryAndAddCollaborator(command, userResult);
      // update 하기 위해 default branch 를 저장
      command.setGithubDefaultBranch(defaultBranch);
    }
  }

  private boolean isRepositoryDeletedOnGithub(
      String githubOwner,
      String githubRepository,
      String githubAuthToken
  ) {
    return githubService.isUniqueRepositoryName(
        new GithubGetRepositoryCommand(
            githubOwner,
            githubRepository,
            githubAuthToken
        )
    );
  }

  private String createRepositoryAndAddCollaborator(UserUpdateCommand command,
      UserResult userResult) {
    GithubRepoInfo githubRepoInfo = githubService.createRepository(
        command.getGithubRepository(), userResult.githubAuthToken()
    );

    UserResult nogiBot = userService.findNogiBot()
        .orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));

    githubService.addCollaborator(
        new GithubAddCollaboratorCommand(
            userResult.githubOwner(), command.getGithubRepository(),
            nogiBot.githubOwner(), userResult.githubAuthToken()
        )
    );
    return githubRepoInfo.defaultBranch();
  }

  public void validateRepositoryName(
      UserFacadeCommand.ValidateRepositoryName command
  ) {
    UserResult userResult = userService.findUserByIdForFacade(command.userId());

    // 현재 사용중인 repository 이름이랑 같은 값인 경우
    if (command.repositoryName().equals(userResult.githubRepository())) {
      throw new GlobalException(F_ALREADY_USING_REPOSITORY_NAME);
    }

    boolean isUniqueName = githubService.isUniqueRepositoryName(
        new GithubGetRepositoryCommand(
            userResult.githubOwner(),
            command.repositoryName(),
            userResult.githubAuthToken()
        )
    );
    if (!isUniqueName) {
      throw new GlobalException(GitResponseCode.F_DUPLICATION_REPO_NAME_GIT);
    }
  }

  public GithubValidateInfo validateGithub(Long userId) {
    UserInfo userInfo = userService.findUserById(userId);

    GithubUserInfo githubInfo = githubClient.getUserInfo(userInfo.githubAuthToken());

    List<GithubUserEmailInfo> githubEmails = githubClient.getUserEmails(
        userInfo.githubAuthToken());

    List<GithubRepoInfo> githubRepositories = githubClient.getUserRepositories(
        userInfo.githubAuthToken());

    return new GithubValidateInfo(userInfo, githubInfo, githubEmails, githubRepositories);
  }
}
