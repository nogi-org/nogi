package kr.co.nogibackend.config.security;

import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
	}

	/**
	 * JWT 액세스 토큰 생성
	 */
	public String generateToken(Long userId) {
		return
			Jwts
				.builder()
				.subject(String.valueOf(userId))
				// todo: user role 필요
				.claim("role", "USER")
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
				.signWith(secretKey)
				.compact();
	}

	/**
	 * JWT 리프레시 토큰 생성
	 */
	public String generateRefreshToken(Long userId) {
		return
			Jwts
				.builder()
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
			jwtParser.parseSignedClaims(token);
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
		// todo: role 필요
		return Long.valueOf(jwtParser.parseSignedClaims(token)
			.getPayload()
			.getSubject());
	}

	/**
	 * 요청 헤더에서 JWT 토큰 추출
	 */
	public String resolveToken(HttpServletRequest request) {
		return
			Optional
				.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
				.filter(token -> token.length() >= 7 && token.substring(0, 7).equalsIgnoreCase("Bearer "))
				.map(token -> token.substring(7))
				.orElse(null);
	}

}

