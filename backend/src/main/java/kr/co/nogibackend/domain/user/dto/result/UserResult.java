package kr.co.nogibackend.domain.user.dto.result;

import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.dto.info.UserInfo;
import org.springframework.util.StringUtils;

public record UserResult(
    Long id,
    User.Role role,
    String notionAccessToken,
    String notionPageId,
    String notionDatabaseId,
    String githubAuthToken,
    String githubRepository,
    String githubDefaultBranch,
    String githubEmail,
    String githubOwner,
    Boolean isNotificationAllowed
) {

  public static UserResult from(
      User user
  ) {
    return new UserResult(
        user.getId(),
        user.getRole(),
        user.getNotionAccessToken(),
        user.getNotionPageId(),
        user.getNotionDatabaseId(),
        user.getGithubAuthToken(),
        user.getGithubRepository(),
        user.getGithubDefaultBranch(),
        user.getGithubEmail(),
        user.getGithubOwner(),
        user.getIsNotificationAllowed()
    );
  }

  public static UserResult from(UserInfo userInfo) {
    return new UserResult(
        userInfo.id(),
        userInfo.role(),
        userInfo.notionAccessToken(),
        userInfo.notionPageId(),
        userInfo.notionDatabaseId(),
        userInfo.githubAuthToken(),
        userInfo.githubRepository(),
        userInfo.githubDefaultBranch(),
        userInfo.githubEmail(),
        userInfo.githubOwner(),
        userInfo.isNotificationAllowed()
    );
  }

  public boolean isUnProcessableToNogi() {
    return !StringUtils.hasText(githubRepository)
        || !StringUtils.hasText(notionDatabaseId)
        || !StringUtils.hasText(notionAccessToken);
  }

  public boolean isNotionDatabaseIdEmpty() {
    return StringUtils.hasText(notionAccessToken)
        && StringUtils.hasText(notionPageId)
        && !StringUtils.hasText(notionDatabaseId);
  }
}
