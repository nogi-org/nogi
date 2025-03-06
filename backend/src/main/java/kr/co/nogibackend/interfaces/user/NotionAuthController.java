package kr.co.nogibackend.interfaces.user;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import kr.co.nogibackend.application.user.UserFacade;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.dto.info.NotionGetAccessTokenResponse;
import kr.co.nogibackend.domain.notion.dto.info.NotionInfo;
import kr.co.nogibackend.domain.notion.dto.request.NotionGetAccessTokenRequest;
import kr.co.nogibackend.infra.notion.NotionFeignClient;
import kr.co.nogibackend.response.service.Response;
import kr.co.nogibackend.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotionAuthController {

  private final UserFacade userFacade;
  private final CookieUtils cookieUtil;
  // TODO infra 단 추상화하기
  private final NotionFeignClient notionFeignClient;
  @Value("${notion.client.id}")
  private String notionClientId;
  @Value("${notion.redirect-uri}")
  private String notionRedirectUri;
  @Value("${notion.client.secret}")
  private String notionClientSecret;
  @Value("${notion.after-login-redirect-uri}")
  private String afterLoginRedirectUrl;

  /**
   * Notion 로그인 URL 반환 (사용자가 클릭하여 Notion 로그인 페이지로 이동)
   */
  @GetMapping("/notion/auth-url")
  public ResponseEntity<?> getNotionAuthUrl() {
    // TODO state 파라미터 설정하기(userId 담기)
    String authUrl = String.format(
        "https://api.notion.com/v1/oauth/authorize?owner=user&client_id=%s&redirect_uri=%s&response_type=code",
        notionClientId, notionRedirectUri
    );

    return Response.success(authUrl);
  }

  /**
   * Notion OAuth 인증 후, 액세스 토큰 요청 및 처리
   */
  @GetMapping("/login/code/notion")
  public ResponseEntity<?> loginByNotion(
      @RequestParam("code") String code,
      @RequestParam("state") String state,
      HttpServletResponse response
  ) throws IOException {
    try {
      String credentials = notionClientId + ":" + notionClientSecret;
      String encodedCredentials = Base64.getEncoder()
          .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

      NotionGetAccessTokenRequest request = NotionGetAccessTokenRequest.of(code, notionRedirectUri);

      ResponseEntity<NotionGetAccessTokenResponse> accessToken = notionFeignClient.getAccessToken(
          "Basic " + encodedCredentials,
          request
      );
      log.info("accessToken: {}", accessToken);

      // TODO retry 5번 비동기로 처리
      Thread.sleep(5000);
      NotionGetAccessTokenResponse body = accessToken.getBody();
      ResponseEntity<NotionInfo<NotionBlockInfo>> blocksFromPage = notionFeignClient.getBlocksFromPage(
          body.accessToken(),
          body.duplicatedTemplateId(),
          null
      );

      NotionInfo<NotionBlockInfo> notionPageResponse = blocksFromPage.getBody();
      List<NotionBlockInfo> results = notionPageResponse.getResults();
      NotionBlockInfo childDatabase = results.stream()
          .filter(v -> v.getType().equals("child_database")).findFirst()
          .orElseThrow(() -> new RuntimeException("Notion Page 에서 Database 를 찾을 수 없습니다."));
      String databaseId = childDatabase.getId();
      System.out.println("databaseId = " + databaseId);

      // TODO user 수정 및 Response 반환
      // TODO Response 에 token 값 없도록 작업

    } catch (Exception e) {
      log.error("Notion OAuth 인증 처리 중 오류 발생", e);
    }

    // TODO redirectUrl 설정하기
    response.sendRedirect("리다이렉터 경로 설정 필요");
    return Response.success();
  }
}
