package kr.co.nogibackend.interfaces.user.dto;

import kr.co.nogibackend.domain.user.dto.command.UserUpdateCommand;

public record UserUpdateRequest(
    String notionBotToken,
    String notionDatabaseId,
    String githubAuthToken,
    String githubRepository,
    String githubDefaultBranch,
    String githubEmail,
    String githubOwner,
    Boolean isNotificationAllowed
) {

  public UserUpdateCommand toCommand(Long id) {
    return UserUpdateCommand.builder()
        .id(id)
        .notionBotToken(notionBotToken())
        .notionDatabaseId(notionDatabaseId())
        .githubAuthToken(githubAuthToken())
        .githubRepository(githubRepository())
        .githubDefaultBranch(githubDefaultBranch())
        .githubEmail(githubEmail())
        .githubOwner(githubOwner())
        .isNotificationAllowed(isNotificationAllowed())
        .build();
  }
}
