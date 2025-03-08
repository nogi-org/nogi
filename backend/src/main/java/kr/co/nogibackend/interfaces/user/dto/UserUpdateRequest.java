package kr.co.nogibackend.interfaces.user.dto;

import kr.co.nogibackend.domain.user.dto.command.UserUpdateCommand;

public record UserUpdateRequest(
    String githubRepository,
    String githubEmail,
    String githubOwner,
    Boolean isNotificationAllowed
) {

  public UserUpdateCommand toCommand(Long id) {
    return UserUpdateCommand.builder()
        .id(id)
        .githubRepository(githubRepository)
        .githubEmail(githubEmail)
        .githubOwner(githubOwner)
        .isNotificationAllowed(isNotificationAllowed)
        .build();
  }
}
