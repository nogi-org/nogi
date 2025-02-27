package kr.co.nogibackend.domain.github.dto.command;

public record GithubGetRepositoryCommand(
    String owner,
    String repoName,
    String token
) {

}
