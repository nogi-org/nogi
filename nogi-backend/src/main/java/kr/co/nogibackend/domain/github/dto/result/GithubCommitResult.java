package kr.co.nogibackend.domain.github.dto.result;

public record GithubCommitResult(
    Long userId,
    String notionPageId,
    String notionBotToken,
    String category,
    String title,
    boolean isSuccess
) {

}
