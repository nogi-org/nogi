package kr.co.nogibackend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import static kr.co.nogibackend.config.security.JwtProvider.ACCESS_TOKEN_VALIDITY;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

	public static final String ACCESS_COOKIE_NAME = "ac_token";

	// access token 쿠키 유효시간 계산
	public static int createAccessTokenCookieExpTime() {
		return (int) (ACCESS_TOKEN_VALIDITY / 1000);
	}

	/**
	 * <h2>✅ 쿠키 생성 (보안 강화)</h2>
	 * <ul>
	 *   <li>SameSite=Strict: CSRF 방어가 강력하지만, 리디렉션 등에서 쿠키 전송이 안 될 수 있음.</li>
	 *   <li>SameSite=Lax: CSRF 방어가 어느 정도 가능하면서도, GET 요청은 허용됨.</li>
	 *   <li>SameSite=None; Secure: 보안이 필요하지만 HTTPS에서만 동작함.</li>
	 * </ul>
	 */
	public static void createCookie(
			HttpServletResponse response,
			String name,
			String value,
			int maxAge,
			boolean isSecure
	) {
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(true);  // JavaScript 접근 차단 (XSS 방어)
		cookie.setSecure(isSecure);  // HTTPS에서만 전송 (네트워크 스니핑 방어)
		cookie.setPath("/");  // 모든 경로에서 유효
		cookie.setMaxAge(maxAge);  // 쿠키 만료 시간 설정

		// ✅ SameSite 속성 추가 (CSRF 방어)
		if (isSecure) {
			response.addHeader("Set-Cookie",
					String.format("%s=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None",
							name, value, maxAge));
		} else {
			response.addHeader("Set-Cookie",
					String.format("%s=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Lax",
							name, value, maxAge));
		}

		response.addCookie(cookie);
	}

	// 쿠키 가져오기
	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		if (request.getCookies() == null) {
			return Optional.empty();
		}
		return
				Arrays
						.stream(request.getCookies())
						.filter(cookie -> cookie.getName().equals(name))
						.findFirst()
						.filter(cookie -> !cookie.getValue().isEmpty());
	}

	// 쿠키 삭제 (보안 강화)
	public static void deleteCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addHeader("Set-Cookie",
				name + "=; Max-Age=0; Path=/; HttpOnly; Secure; SameSite=None");
		response.addCookie(cookie);
	}


}
