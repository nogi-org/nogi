package kr.co.nogibackend.interfaces.user;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    Date expirationDate = getExpirationDate(10); // 토큰 만료 시간 설정

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
  @GetMapping("/login/code/notion")
  public ResponseEntity<?> loginByNotion(
      @RequestParam(value = "code", required = false) String code,
      @RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "state", required = false) String state,
      HttpServletResponse response
  ) throws IOException {
    // 유저가 로그인을 취소한 경우
    if (error != null) {
      String encodedMessage = encodeParam("NOGI 서비스를 이용하기 위해서는 Notion 인증이 필요합니다.");
      String redirectUrl = new StringBuilder(afterLoginRedirectUrl)
          .append("?isSuccess=false")
          .append("&type=NOTION")
          .append("&message=").append(encodedMessage)
          .toString();

      response.sendRedirect(redirectUrl);
      return Response.success();
    }

    String[] split = state.split(":");
    String token = split[0];
    String event = split[1];

    // 토큰이 유효한지 체크
    if (!jwtProvider.validateToken(token)) {
      String encodedMessage = encodeParam("유효하지 않은 토큰 입니다. 적절한 경로로 이용해주세요.");
      String redirectUrl = new StringBuilder(afterLoginRedirectUrl)
          .append("?isSuccess=false")
          .append("&type=NOTION")
          .append("&message=").append(encodedMessage)
          .toString();

      response.sendRedirect(redirectUrl);
      return Response.success();
    }

    Long userId = jwtProvider.getUserInfoFromToken(token).getUserId();

    UserLoginByNotionInfo userLoginByNotionInfo = userFacade.loginByNotion(
        new NotionLogin(userId, notionClientId, notionClientSecret, notionRedirectUri, code)
    );

    String redirectUrl = new StringBuilder(afterLoginRedirectUrl)
        .append("?isSuccess=").append(userLoginByNotionInfo.isSuccess())
        .append("&message=").append(encodeParam(userLoginByNotionInfo.message()))
        .append("&event=").append(event)
        .toString();

    response.sendRedirect(redirectUrl);
    return Response.success();
  }

  /**
   * URL 인코딩을 수행하는 유틸리티 메서드
   */
  private String encodeParam(String param) {
    try {
      return URLEncoder.encode(param, StandardCharsets.UTF_8);
    } catch (Exception e) {
      log.error("URL 인코딩 중 오류 발생: {}", param, e);
      return "encoding_error"; // 인코딩 실패 시 원본 값 반환
    }
  }

  /**
   * 특정 시간(분) 후 만료되는 Date 객체 생성
   */
  private Date getExpirationDate(int minutes) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, minutes);
    return calendar.getTime();
  }
}
