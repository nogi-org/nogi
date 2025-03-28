package kr.co.nogibackend.application.user;

import java.util.List;
import kr.co.nogibackend.domain.github.GithubClient;
import kr.co.nogibackend.domain.github.dto.info.GithubRepoInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserEmailInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubValidateInfo;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.domain.user.dto.info.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidateFacade {

  private final UserService userService;
  private final GithubClient githubClient;

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
