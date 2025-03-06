package kr.co.nogibackend.domain.user.dto.result;

import kr.co.nogibackend.domain.user.User;

public record UserSinUpOrUpdateResult(
    Long id,
    User.Role role,
    String notionPageId,
    String githubOwner,
    boolean isSinUp
) {

  public static UserSinUpOrUpdateResult from(User user, boolean isSinUp) {
    return new UserSinUpOrUpdateResult(
        user.getId(),
        user.getRole(),
        user.getNotionPageId(),
        user.getGithubOwner(),
        isSinUp
    );
  }
}
