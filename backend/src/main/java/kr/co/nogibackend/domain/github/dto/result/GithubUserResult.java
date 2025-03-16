package kr.co.nogibackend.domain.github.dto.result;

import kr.co.nogibackend.domain.github.dto.info.GithubUserEmailInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserInfo;

public record GithubUserResult(
    Long id,
    String email,
    String owner
) {

  public static GithubUserResult from(
      GithubUserInfo userInfo,
      GithubUserEmailInfo userEmailInfo
  ) {
    return new GithubUserResult(
        userInfo.id(),
        userEmailInfo.email(),
        userInfo.login()
    );
  }
}
