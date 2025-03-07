package kr.co.nogibackend.application.user;

import static kr.co.nogibackend.response.code.GitResponseCode.F_ALREADY_USING_REPOSITORY_NAME;
import static kr.co.nogibackend.response.code.UserResponseCode.F_NOT_FOUND_USER;

import kr.co.nogibackend.application.user.dto.UserFacadeCommand;
import kr.co.nogibackend.config.audit.AuditContext;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.config.security.JwtProvider;
import kr.co.nogibackend.domain.github.GithubService;
import kr.co.nogibackend.domain.github.dto.command.GithubAddCollaboratorCommand;
import kr.co.nogibackend.domain.github.dto.command.GithubGetRepositoryCommand;
import kr.co.nogibackend.domain.github.dto.info.GithubRepoInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubOAuthAccessTokenRequest;
import kr.co.nogibackend.domain.github.dto.result.GithubUserResult;
import kr.co.nogibackend.domain.notion.NotionService;
import kr.co.nogibackend.domain.notion.dto.result.NotionGetAccessResult;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.domain.user.dto.command.UserUpdateCommand;
import kr.co.nogibackend.domain.user.dto.info.UserInfo;
import kr.co.nogibackend.domain.user.dto.info.UserLoginByGithubInfo;
import kr.co.nogibackend.domain.user.dto.info.UserLoginByNotionInfo;
import kr.co.nogibackend.domain.user.dto.result.UserResult;
import kr.co.nogibackend.domain.user.dto.result.UserSinUpOrUpdateResult;
import kr.co.nogibackend.response.code.GitResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final GithubService githubService;
  private final NotionService notionService;
  private final UserService userService;
  private final JwtProvider jwtProvider;

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
          "success github login"
      );
    } catch (Exception e) {
      return UserLoginByGithubInfo.from(true, "fail github login");
    }
  }

  public UserLoginByNotionInfo loginByNotion(UserFacadeCommand.NotionLogin command) {
    try {
      // 1. access token 가져오기
      NotionGetAccessResult notionResult = notionService.getAccessToken(
          command.getBasicToken(),
          command.code(),
          command.notionRedirectUri()
      );

      // 2. database 정보 가져오기 (비동기)
      // TODO : 비동기로 처리하기, event driven 방식으로 리팩터링하기
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      String notionDatabaseId = notionService.getNotionDatabaseInfo(
          notionResult.notionAccessToken(),
          notionResult.notionPageId()
      );
      userService.updateUser(
          UserUpdateCommand.builder()
              .id(command.userId())
              .notionDatabaseId(notionDatabaseId)
              .build()
      );

      // 3. AuditContext 에 저장(DB auditing 을 위함) -> callUrl 로 동작하는 메서드라 따로 처리
      AuditContext.setUserId(command.userId());

      // 4. user 정보 저장하기
      userService.updateUser(
          UserUpdateCommand.builder()
              .id(command.userId())
              .notionAccessToken(notionResult.notionAccessToken())
              .notionPageId(notionResult.notionPageId())
              .build()
      );

      return new UserLoginByNotionInfo(true, "notion connection success");
    } catch (Exception e) {
      return new UserLoginByNotionInfo(false, "notion connection fail");
    }
  }


  public UserInfo updateUserAndCreateRepo(UserUpdateCommand command) {
    // 1. user 정보 가져오기
    UserResult userResult = userService.findUserByIdForFacade(command.getId());

    // 2. Repository 생성 또는 업데이트
    handleRepositoryCreationOrUpdate(command, userResult);

    // 3. DB 에 user 정보 업데이트
    return userService.updateUser(command);
  }

  private void handleRepositoryCreationOrUpdate(UserUpdateCommand command, UserResult userResult) {
    String existingRepo = userResult.githubRepository();
    String newRepo = command.getGithubRepository();

    // 기존 repository가 없으면 새로 생성
    if (existingRepo == null) {
      createRepositoryAndAddCollaborator(command, userResult);
      return;
    }

    boolean isDeletedOnGithub = isRepositoryDeletedOnGithub(userResult);

    // GitHub에서 삭제된 상태일 때
    if (isDeletedOnGithub) {
      createRepositoryAndAddCollaborator(command, userResult);
      return;
    }

    // repository 이름이 변경된 경우
    if (!existingRepo.equals(newRepo)) {
      githubService.updateRepository(
          userResult.githubOwner(), existingRepo, newRepo, userResult.githubAuthToken()
      );
    }
  }

  private boolean isRepositoryDeletedOnGithub(UserResult userResult) {
    return githubService.isUniqueRepositoryName(
        new GithubGetRepositoryCommand(
            userResult.githubOwner(), userResult.githubRepository(), userResult.githubAuthToken()
        )
    );
  }

  private void createRepositoryAndAddCollaborator(UserUpdateCommand command,
      UserResult userResult) {
    GithubRepoInfo githubRepoInfo = githubService.createRepository(
        command.getGithubRepository(), userResult.githubAuthToken()
    );
    command.setGithubDefaultBranch(githubRepoInfo.defaultBranch());

    UserResult nogiBot = userService.findNogiBot()
        .orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));

    githubService.addCollaborator(
        new GithubAddCollaboratorCommand(
            userResult.githubOwner(), command.getGithubRepository(),
            nogiBot.githubOwner(), userResult.githubAuthToken()
        )
    );
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
}
