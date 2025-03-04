package kr.co.nogibackend.domain.github.dto.request;

public record GithubCreateOrUpdateContentRequest(
    String message,
    String content,
    GithubCommitter committer
) {

  public record GithubCommitter(
      String name,
      String email
  ) {

  }
}
