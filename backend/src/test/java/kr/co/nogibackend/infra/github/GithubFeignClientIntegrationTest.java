package kr.co.nogibackend.infra.github;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kr.co.nogibackend.domain.github.dto.info.GithubUserEmailInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubAddCollaboratorRequest;
import kr.co.nogibackend.domain.github.dto.request.GithubCreateIssueRequest;
import kr.co.nogibackend.environment.GithubTestEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/*
  Package Name : kr.co.nogibackend.infra.github
  File Name    : GithubFeignClientIntegrationTest
  Author       : won taek oh
  Created Date : 25. 2. 9.
  Description  : GithubFeignClient 통합 테스트
 */
@Slf4j
class GithubFeignClientIntegrationTest extends GithubTestEnvironment {

  @Autowired
  private GithubFeignClient githubFeignClient;

  @Test
  @DisplayName("저장소의 협력자에 nogi-bot을 추가하고 nogi-bot이 owner에게 이슈를 생성한다.")
  void testCreateIssue() {
    // when // then
    githubFeignClient.addCollaborator(
        testUserOwner,
        testUserRepo,
        "nogi-bot",
        new GithubAddCollaboratorRequest(null),
        testUserToken
    );
    githubFeignClient.createIssue(
        testUserOwner,
        testUserRepo,
        new GithubCreateIssueRequest(
            "Test Issue",
            "Test Issue Body @" + testUserOwner,
            List.of(testUserOwner)
        ),
        nogiBotToken
    );
  }

  @Test
  @DisplayName("토큰으로 유저의 정보를 조회하면 유저의 owner 를 조회할 수 있다.")
  public void getUserInfo() {
    // when
    GithubUserInfo userInfo = githubFeignClient.getUserInfo(testUserToken);
    // then
    assertThat(userInfo.login()).isEqualTo(testUserOwner);
  }

  @Test
  @DisplayName("토큰으로 유저의 이메일 정보를 조회하면 이메일 정보를 조회할 수 있고, primary 이메일이 존재한다.")
  public void getUserEmailInfo() {
    // when
    List<GithubUserEmailInfo> userEmailInfos = githubFeignClient.getUserEmailInfo(testUserToken);

    // then
    assertThat(userEmailInfos).isNotEmpty();

    GithubUserEmailInfo githubUserEmailInfo = userEmailInfos.stream()
        .filter(GithubUserEmailInfo::primary)
        .findFirst()
        .orElse(null);
    assertThat(githubUserEmailInfo).isNotNull();
  }
}
