package kr.co.nogibackend.interfaces.user;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import kr.co.nogibackend.application.user.UserFacade;
import kr.co.nogibackend.application.user.dto.UserFacadeCommand.NotionLogin;
import kr.co.nogibackend.config.security.Auth;
import kr.co.nogibackend.config.security.JwtProvider;
import kr.co.nogibackend.domain.user.dto.info.UserLoginByNotionInfo;
import kr.co.nogibackend.infra.notion.NotionFeignClient;
import kr.co.nogibackend.response.service.Response;
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
  public ResponseEntity<?> getNotionAuthUrl(
      Auth auth,
      @RequestParam(value = "event") String event // notion 로그인 or notion new 생성
  ) {

    // TODO 추가 코드 리팩터링
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.MINUTE, 10);
    Date expirationDate = calendar.getTime();

    String token = jwtProvider.generateToken(auth.getUserId(), auth.getRole(), expirationDate);

    String authUrl = String.format(
        "https://api.notion.com/v1/oauth/authorize?owner=user&client_id=%s&redirect_uri=%s&response_type=code&state=%s",
        notionClientId, notionRedirectUri, token + ":" + event
    );

    return Response.success(authUrl);
  }

  /**
   * Notion OAuth 인증 후, 액세스 토큰 요청 및 처리
   */
  /**
   * Notion OAuth 인증 후, 액세스 토큰 요청 및 처리
   */
  @GetMapping("/login/code/notion")
  public ResponseEntity<?> loginByNotion(
      @RequestParam(value = "code", required = false) String code,
      @RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "state", required = false) String state,
      HttpServletResponse response
  ) throws IOException {
    // 유저가 로그인을 취소한 경우
    if (error != null) {
      String redirectUrl = new StringBuilder(afterLoginRedirectUrl)
          .append("?isSuccess=false")
          .append("&type=NOTION")
          .append("&message=notion login cancel")
          .toString();

      response.sendRedirect(redirectUrl);
      return Response.success();
    }

    String[] split = state.split(":");
    String token = split[0];
    String event = split[1];

    // 토큰이 유효한지 체크
    if (!jwtProvider.validateToken(token)) {
      String redirectUrl = new StringBuilder(afterLoginRedirectUrl)
          .append("?isSuccess=false")
          .append("&type=NOTION")
          .append("&message=Invalid token")
          .toString();

      response.sendRedirect(redirectUrl);
      return Response.success();
    }

    Long userId = jwtProvider.getUserInfoFromToken(state).getUserId();

    UserLoginByNotionInfo userLoginByNotionInfo = userFacade.loginByNotion(
        new NotionLogin(userId, notionClientId, notionClientSecret, notionRedirectUri, code)
    );

    String redirectUrl = new StringBuilder(afterLoginRedirectUrl)
        .append("?isSuccess=").append(userLoginByNotionInfo.isSuccess())
        .append("&message=").append(userLoginByNotionInfo.message())
        .append("&event=").append(event)
        .toString();

    response.sendRedirect(redirectUrl);
    return Response.success();
  }


}
