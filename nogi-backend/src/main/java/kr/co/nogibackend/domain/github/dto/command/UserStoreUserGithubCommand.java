package kr.co.nogibackend.domain.github.dto.command;

public record UserStoreUserGithubCommand(
    String accessToken,
    String email,
    String owner
) {

}
