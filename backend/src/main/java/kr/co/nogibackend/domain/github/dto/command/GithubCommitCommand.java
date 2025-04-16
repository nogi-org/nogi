package kr.co.nogibackend.domain.github.dto.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.co.nogibackend.domain.notion.dto.result.CompletedPageMarkdownResult;
import kr.co.nogibackend.domain.user.NogiHistoryType;
import kr.co.nogibackend.domain.user.dto.result.UserCheckTILResult;
import kr.co.nogibackend.domain.user.dto.result.UserResult;

public record GithubCommitCommand(
    Long userId,               // 유저 ID
    String githubOwner,           // 유저 이름 (github owner)
    String githubRepository,   // 깃허브 레포지토리
    String githubBranch,       // 깃허브 브랜치
    String githubEmail,       // 깃허브 이메일
    String notionPageId,        // 노션 페이지 ID
    String notionBotToken,     // 노션 인증 토큰
    NogiHistoryType type,       // 히스토리 타입 (생성, 수정 등)
    String newCategory,         // 새로운 카테고리 (디렉토리 하위 구조)
    String newTitle,            // 새로운 제목 (파일명)
    String prevCategory,        // 이전 카테고리
    String prevTitle,           // 이전 제목
    String commitDate,          // 커밋 일자
    String commitMessage,        // 커밋 메시지
    String content,             // markdown 파일 내용
    String githubToken,         // 깃허브 토큰
    List<ImageOfGithub> images, // 이미지 정보
    NogiBot nogiBot
) {

  /**
   * 📌 List<NotionStartTILResult>와 List<UserCheckTILResult>를 조합하여 List<GithubCommitCommand> 생성
   */
  public static List<GithubCommitCommand> of(
      List<CompletedPageMarkdownResult> notionResults,
      List<UserCheckTILResult> userCheckResults,
      UserResult nogiBotResult
  ) {
    // ✅ userCheckResults를 notionPageId 기준으로 매핑 (빠른 조회를 위해 Map 사용)
    Map<String, UserCheckTILResult> userCheckMap = userCheckResults.stream()
        .collect(Collectors.toMap(UserCheckTILResult::notionPageId, Function.identity()));

    // ✅ notionResults를 기반으로 GithubCommitCommand 리스트 생성
    return notionResults.stream()
        .map(notionResult -> {
          UserCheckTILResult userCheckTILResult = userCheckMap.get(notionResult.notionPageId());

          return new GithubCommitCommand(
              notionResult.userId(),
              userCheckTILResult.userName(),
              userCheckTILResult.repository(),
              userCheckTILResult.branch(),
              userCheckTILResult.githubEmail(),
              notionResult.notionPageId(),
              userCheckTILResult.notionBotToken(),
              userCheckTILResult.type(),
              notionResult.category(),
              notionResult.title(),
              userCheckTILResult.prevCategory(),
              userCheckTILResult.prevTitle(),
              notionResult.commitDate(),
              notionResult.commitMessage(),
              notionResult.content(),
              userCheckTILResult.githubToken(),
              notionResult.images().stream()
                  .map(image -> new ImageOfGithub(image.fileEnc64(), image.fileName(),
                      image.filePath()))
                  .collect(Collectors.toList()),
              new NogiBot(
                  nogiBotResult.githubAuthToken(),
                  nogiBotResult.githubOwner(),
                  nogiBotResult.githubRepository(),
                  nogiBotResult.githubDefaultBranch(),
                  nogiBotResult.githubEmail()
              )
          );
        })
        .collect(Collectors.toList());
  }

  private String getMarkdownFilePath() {
    String safeCategory = newCategory.replace("/", "_");
    String safeTitle = newTitle.replace("/", "_");
    return safeCategory + "/" + safeTitle + ".md";
  }

  private String getPrevMarkdownFilePath() {
    String safeCategory = prevCategory.replace("/", "_");
    String safeTitle = prevTitle.replace("/", "_");
    return safeCategory + "/" + safeTitle + ".md";
  }

  public Map<String, String> prepareFiles() {
    Map<String, String> fileMap = new HashMap<>();
    addMarkdownFile(fileMap);
    addHistoryFile(fileMap);
    return fileMap;
  }

  public Map<String, String> prepareImageFiles(String resourcesBasePath) {
    Map<String, String> fileMap = new HashMap<>();
    addImageFiles(fileMap, resourcesBasePath);
    return fileMap;
  }

  public String getCommitMessage() {
    if (commitMessage != null && !commitMessage.isBlank()) {
      return commitMessage;
    }
    // 커밋 Message 를 작성하지 않은 경우 기본 메시지 반환
    return """
        %s/%s %s""".formatted(newCategory, newTitle,
        type == NogiHistoryType.CREATE_OR_UPDATE_CONTENT ? "작성" : "수정");
  }

  private void addMarkdownFile(Map<String, String> fileMap) {
    fileMap.put(getMarkdownFilePath(), this.content);
  }

  private void addImageFiles(Map<String, String> fileMap, String resourcesBasePath) {
    images.forEach(image -> {
          // basePath 를 제외한 상대 경로를 key로 사용
          String imageFilePath = image.getImageFilePath();
          String relativePath = imageFilePath.replaceFirst(resourcesBasePath, "");
          fileMap.put(relativePath, image.getImageFile());
        }
    );
  }

  private void addHistoryFile(Map<String, String> fileMap) {
    if (
      // 제목 or 카테고리가 수정되었을 경우 이전 md file 을 삭제하기 위해 null 값을 value 로 설정
        type == NogiHistoryType.UPDATE_TITLE ||
            type == NogiHistoryType.UPDATE_CATEGORY
    ) {
      fileMap.put(getPrevMarkdownFilePath(), null);
    }
  }

  public record NogiBot(
      String githubToken,
      String githubOwner,
      String githubRepository,
      String githubBranch,
      String githubEmail
  ) {

  }

  public record ImageOfGithub(
      String fileEnc64, // 이미지 파일
      String fileName,  // 이미지 파일명
      String filePath   // 이미지 파일 경로
  ) {

    public String getImageFilePath() {
      return filePath;
    }

    public String getImageFile() {
      return fileEnc64;
    }
  }
}
