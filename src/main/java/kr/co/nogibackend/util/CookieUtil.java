package kr.co.nogibackend.util;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// access token 을 쿠키에 저장할 경우 사용
@Component
public class CookieUtil {

	/**
	 * 쿠키 생성
	 */
	public void createCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	/**
	 * 쿠키 가져오기
	 */
	public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		if (request.getCookies() == null)
			return Optional.empty();
		return Arrays.stream(request.getCookies())
			.filter(cookie -> cookie.getName().equals(name))
			.findFirst();
	}

	/**
	 * 쿠키 삭제
	 */
	public void deleteCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}