package kr.co.nogibackend.interfaces.user.dto;

import java.util.List;
import kr.co.nogibackend.domain.github.dto.info.GithubRepoInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserEmailInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubUserInfo;
import kr.co.nogibackend.domain.github.dto.info.GithubValidateInfo;

public record UserValidateGithubResponse(
    UserResponse user, // 유저 정보
    boolean isGithubValid, // GitHub 유효한지
    boolean isGithubOwnerValid, // GitHub owner 유효한지
    boolean isGithubEmailValid, // GitHub email 유효한지
    boolean isGithubRepositoryValid, // GitHub repository 유효한지
    GithubUserResponse githubUser, // GitHub user 정보
    List<GithubRepositoryResponse> githubRepositories, // GitHub repository 정보
    List<GithubEmailResponse> githubEmails // GitHub email 정보
) {

  public static UserValidateGithubResponse from(GithubValidateInfo info) {
    UserResponse userResponse = UserResponse.from(info.user());
    GithubUserResponse githubUserResponse = GithubUserResponse.from(info.githubUserInfo());
    List<GithubRepositoryResponse> repositories = info.githubRepositories().stream()
        .map(GithubRepositoryResponse::from)
        .toList();
    List<GithubEmailResponse> emails = info.githubEmails().stream()
        .map(GithubEmailResponse::from)
        .toList();

    // ✅ GitHub Owner 검증: User의 owner명과 GitHub의 login명이 같은지 확인
    boolean isOwnerValid = info.user().githubOwner()
        .equalsIgnoreCase(info.githubUserInfo().login());

    // ✅ GitHub Email 검증: GitHub 이메일 중 primary 인 것을 찾아서 User의 email과 같은지 확인
    boolean isEmailValid = info.githubEmails().stream()
        .filter(GithubUserEmailInfo::primary)
        .anyMatch(email -> email.email().equalsIgnoreCase(info.user().githubEmail()));

    // ✅ GitHub Repository 검증: 사용자의 GitHub 저장소 중 하나라도 같은 이름이 있는지 확인
    boolean isRepositoryValid = info.githubRepositories().stream()
        .anyMatch(repo -> repo.name().equalsIgnoreCase(info.user().githubRepository()));

    // ✅ 최종 GitHub 유효성 검사 (위 3가지 조건이 모두 만족해야 true)
    boolean isGithubValid = isOwnerValid && isEmailValid && isRepositoryValid;

    return new UserValidateGithubResponse(
        userResponse,
        isGithubValid,
        isOwnerValid,
        isEmailValid,
        isRepositoryValid,
        githubUserResponse,
        repositories,
        emails
    );
  }

  // ✅ GitHub User 응답 객체
  public record GithubUserResponse(
      String login,
      String avatarUrl,
      String htmlUrl,
      String name,
      String email,
      Integer publicRepos,
      Integer followers,
      Integer following
  ) {

    public static GithubUserResponse from(GithubUserInfo info) {
      return new GithubUserResponse(
          info.login(),
          info.avatar_url(),
          info.html_url(),
          info.name(),
          info.email(),
          info.public_repos(),
          info.followers(),
          info.following()
      );
    }
  }

  // ✅ GitHub Repository 응답 객체
  public record GithubRepositoryResponse(
      String name,
      String fullName,
      String htmlUrl,
      Boolean isPrivate,
      String defaultBranch,
      Integer stargazersCount,
      Integer forksCount
  ) {

    public static GithubRepositoryResponse from(GithubRepoInfo info) {
      return new GithubRepositoryResponse(
          info.name(),
          info.fullName(),
          info.htmlUrl(),
          info.privateRepo(),
          info.defaultBranch(),
          info.stargazersCount(),
          info.forksCount()
      );
    }
  }

  // ✅ GitHub Email 응답 객체
  public record GithubEmailResponse(
      String email,
      boolean primary,
      boolean verified
  ) {

    public static GithubEmailResponse from(GithubUserEmailInfo info) {
      return new GithubEmailResponse(
          info.email(),
          info.primary(),
          info.verified()
      );
    }
  }
}
