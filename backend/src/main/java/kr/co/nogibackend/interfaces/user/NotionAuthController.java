package kr.co.nogibackend.interfaces.user;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import kr.co.nogibackend.application.user.UserFacade;
import kr.co.nogibackend.config.security.Auth;
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
    String authUrl = String.format(
        "https://api.notion.com/v1/oauth/authorize?owner=user&client_id=%s&redirect_uri=%s&response_type=code",
        notionClientId, notionRedirectUri
    );

    return Response.success(authUrl);
  }

  // TODO infra 단 추상화하기
  private final NotionFeignClient notionFeignClient;

  /**
   * Notion OAuth 인증 후, 액세스 토큰 요청 및 처리
   */
  @GetMapping("/login/code/notion")
  public ResponseEntity<?> loginByNotion(
      @RequestParam("code") String code,
      HttpServletResponse response,
      Auth auth
  ) throws IOException {
    System.out.println("auth = " + auth);
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

      NotionGetAccessTokenResponse body = accessToken.getBody();
      // TODO Block type copy_indicator is not supported via the API. 에러가 자꾸 발생하뮤ㅠㅠㅠㅠㅠㅠㅠ
      // TODO POSTMAN 으로 직접요청하면 type 이 copy_indicator 인게 없는데 이런 에러가남ㅠㅠㅠㅠㅠㅠㅠ
      ResponseEntity<NotionInfo<NotionBlockInfo>> blocksFromPage = notionFeignClient.getBlocksFromPage(
          body.accessToken(),
          body.duplicatedTemplateId(),
          null
      );//1ad7e38f-6917-81b1-991e-f67f269d9587
      //1ad7e38f-6917-81b0-ba08-cf5145ee5e71
      NotionInfo<NotionBlockInfo> notionPageResponse = blocksFromPage.getBody();
      List<NotionBlockInfo> results = notionPageResponse.getResults();
      NotionBlockInfo childDatabase = results.stream()
          .filter(v -> v.getType().equals("child_database")).findFirst()
          .orElseThrow(() -> new RuntimeException("Notion Page 에서 Database 를 찾을 수 없습니다."));
      String databaseId = childDatabase.getId();
      System.out.println("databaseId = " + databaseId);


    } catch (Exception e) {
      log.error("Notion OAuth 인증 처리 중 오류 발생", e);
    }

    response.sendRedirect("리다이렉터 경로 설정 필요");
    return Response.success();
  }
}
