package kr.co.nogibackend.interfaces.user;

import static kr.co.nogibackend.util.CookieUtils.ACCESS_COOKIE_NAME;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.nogibackend.application.user.UserFacade;
import kr.co.nogibackend.application.user.dto.UserFacadeCommand.NotionLogin;
import kr.co.nogibackend.config.security.JwtProvider;
import kr.co.nogibackend.domain.user.dto.info.UserLoginByNotionInfo;
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
  private final JwtProvider jwtProvider;

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
  public ResponseEntity<?> getNotionAuthUrl(HttpServletRequest request) {
    // TODO Security 로 빼야하나?
    Cookie cookie = CookieUtils.getCookie(request, ACCESS_COOKIE_NAME)
        .orElseThrow(() -> new RuntimeException("쿠키가 존재하지 않습니다."));
    String accessToken = cookie.getValue();

    String authUrl = String.format(
        "https://api.notion.com/v1/oauth/authorize?owner=user&client_id=%s&redirect_uri=%s&response_type=code&state=%s",
        notionClientId, notionRedirectUri, accessToken
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

    Long userId = jwtProvider.getUserInfoFromToken(state).getUserId();

    UserLoginByNotionInfo userLoginByNotionInfo = userFacade.loginByNotion(
        new NotionLogin(userId, notionClientId, notionClientSecret, notionRedirectUri, code)
    );

    String redirectUrl = String.format(
        "%s?isSuccess=%s&message=%s",
        afterLoginRedirectUrl,
        userLoginByNotionInfo.isSuccess(),
        userLoginByNotionInfo.message()
    );

    response.sendRedirect(redirectUrl);
    return Response.success();
  }
}
