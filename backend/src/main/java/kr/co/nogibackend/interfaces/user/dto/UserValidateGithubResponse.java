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
      Long id,
      String nodeId,
      String name,
      String fullName,
      GithubOwnerResponse owner,
      Boolean privateRepo,
      String htmlUrl,
      String description,
      Boolean fork,
      String url,
      String defaultBranch,
      Boolean hasIssues,
      Boolean hasProjects,
      Boolean hasWiki,
      Boolean hasPages,
      Boolean hasDownloads,
      Boolean archived,
      Boolean disabled,
      String visibility,
      String createdAt,
      String updatedAt,
      String pushedAt,
      Integer forksCount,
      Integer stargazersCount,
      Integer watchersCount,
      Integer size,
      Integer openIssuesCount,
      Boolean isTemplate,
      GithubLicenseResponse license,
      List<String> topics,
      GithubPermissionsResponse permissions
  ) {

    public static GithubRepositoryResponse from(GithubRepoInfo info) {
      return new GithubRepositoryResponse(
          info.id(),
          info.nodeId(),
          info.name(),
          info.fullName(),
          GithubOwnerResponse.from(info.owner()),
          info.privateRepo(),
          info.htmlUrl(),
          info.description(),
          info.fork(),
          info.url(),
          info.defaultBranch(),
          info.hasIssues(),
          info.hasProjects(),
          info.hasWiki(),
          info.hasPages(),
          info.hasDownloads(),
          info.archived(),
          info.disabled(),
          info.visibility(),
          info.createdAt(),
          info.updatedAt(),
          info.pushedAt(),
          info.forksCount(),
          info.stargazersCount(),
          info.watchersCount(),
          info.size(),
          info.openIssuesCount(),
          info.isTemplate(),
          info.license() != null ? GithubLicenseResponse.from(info.license()) : null,
          info.topics(),
          info.permissions() != null ? GithubPermissionsResponse.from(info.permissions()) : null
      );
    }
  }

  // ✅ GitHub Owner 응답 객체
  public record GithubOwnerResponse(
      String login,
      Long id,
      String nodeId,
      String avatarUrl,
      String url,
      String htmlUrl,
      String type,
      Boolean siteAdmin
  ) {

    public static GithubOwnerResponse from(GithubRepoInfo.GithubOwnerInfo info) {
      return new GithubOwnerResponse(
          info.login(),
          info.id(),
          info.nodeId(),
          info.avatarUrl(),
          info.url(),
          info.htmlUrl(),
          info.type(),
          info.siteAdmin()
      );
    }
  }

  // ✅ GitHub License 응답 객체
  public record GithubLicenseResponse(
      String key,
      String name,
      String url,
      String spdxId,
      String nodeId,
      String htmlUrl
  ) {

    public static GithubLicenseResponse from(GithubRepoInfo.GithubLicenseInfo info) {
      return new GithubLicenseResponse(
          info.key(),
          info.name(),
          info.url(),
          info.spdxId(),
          info.nodeId(),
          info.htmlUrl()
      );
    }
  }

  // ✅ GitHub Permissions 응답 객체
  public record GithubPermissionsResponse(
      Boolean admin,
      Boolean push,
      Boolean pull
  ) {

    public static GithubPermissionsResponse from(GithubRepoInfo.GithubPermissions info) {
      return new GithubPermissionsResponse(
          info.admin(),
          info.push(),
          info.pull()
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
