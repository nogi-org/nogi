package kr.co.nogibackend.util;

public class AuthTokenUtil {

	private static final String AUTHORIZATION_SCHEME = "Bearer";

	private AuthTokenUtil() {
		// 인스턴스화 방지 (유틸 클래스는 객체 생성을 막는 것이 일반적)
	}

	public static String generateBearerToken(String accessToken) {
		if (accessToken == null || accessToken.isBlank()) {
			throw new IllegalArgumentException("Access token must not be null or blank");
		}
		return String.join(" ", AUTHORIZATION_SCHEME, accessToken);
	}
}