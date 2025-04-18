package kr.co.nogibackend.domain.user.dto.command;

import kr.co.nogibackend.domain.github.dto.result.GithubUserResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateCommand {

  private Long id;
  private String notionAccessToken;
  private String notionPageId;
  private String notionDatabaseId;
  private String githubAuthToken;
  private String githubRepository;
  @Setter
  private String githubDefaultBranch;
  private String githubEmail;
  private String githubOwner;
  private Boolean isNotificationAllowed;
  private String selfIntroduction;

  public static UserUpdateCommand fromGithubLogin(
      GithubUserResult githubUserResult,
      String accessToken
  ) {
    return UserUpdateCommand.builder()
        .githubAuthToken(accessToken)
        .githubEmail(githubUserResult.email())
        .githubOwner(githubUserResult.owner())
        .build();
  }
}
