package kr.co.nogibackend.util;

import static kr.co.nogibackend.config.security.JwtProvider.ACCESS_TOKEN_VALIDITY;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Component;

/*
  Package Name : kr.co.nogibackend.util
  File Name    : CookieUtil
  Author       : won taek oh
  Created Date : 25. 2. 13.
  Description  :
 */
@Component
public class CookieUtil {

  public static final String ACCESS_COOKIE_NAME = "ac_token";

  // access token 쿠키 유효시간 계산
  public static int createAccessTokenCookieExpTime() {
    return (int) (ACCESS_TOKEN_VALIDITY / 1000);
  }

  // 쿠키 생성
  public void createCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  // 쿠키 가져오기
  public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    if (request.getCookies() == null) {
      return Optional.empty();
    }
    return
        Arrays
            .stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(name))
            .findFirst();
  }

  // 쿠기 삭제
  public void deleteCookie(HttpServletResponse response, String name) {
    Cookie cookie = new Cookie(name, null);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }

}
