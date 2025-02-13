package kr.co.nogibackend.config.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

	private static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60 * 24;  // 24시간
	private static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7;  // 7일
	private final SecretKey secretKey;
	private final JwtParser jwtParser;

	public JwtProvider(@Value("${jwt.secret}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));  // 🔥 Secret Key 변경
		this.jwtParser = Jwts.parser().verifyWith(secretKey).build();  // 🔥 JwtParser 생성
	}

	/**
	 * JWT 액세스 토큰 생성
	 */
	public String generateToken(Long userId) {
		return Jwts.builder()
			.subject(String.valueOf(userId))
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
			.signWith(secretKey)
			.compact();
	}

	/**
	 * JWT 리프레시 토큰 생성
	 */
	public String generateRefreshToken(Long userId) {
		return Jwts.builder()
			.subject(String.valueOf(userId))
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
			.signWith(secretKey)
			.compact();
	}

	/**
	 * JWT 검증
	 */
	public boolean validateToken(String token) {
		try {
			jwtParser.parseSignedClaims(token);  // 🔥 새 방식 적용
			return true;
		} catch (ExpiredJwtException e) {
			log.error("JWT Token 만료");
		} catch (JwtException e) {
			log.error("JWT Token 검증 실패");
		}
		return false;
	}

	/**
	 * JWT에서 사용자 ID 추출
	 */
	public Long getUserIdFromToken(String token) {
		return Long.valueOf(jwtParser.parseSignedClaims(token)
			.getPayload()
			.getSubject());
	}

	/**
	 * 요청 헤더에서 JWT 토큰 추출
	 */
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}

