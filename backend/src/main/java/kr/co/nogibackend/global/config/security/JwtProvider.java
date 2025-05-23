package kr.co.nogibackend.global.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import kr.co.nogibackend.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

	public static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60 * 24;  // 24시간
	public static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7;  // 7일
	private final SecretKey secretKey;
	private final JwtParser jwtParser;

	public JwtProvider(@Value("${jwt.secret}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
	}

	// JWT 액세스 토큰 생성
	public String generateToken(Long userId, User.Role role) {
		return
				Jwts
						.builder()
						.subject(String.valueOf(userId))
						.claim("role", role.name())
						.issuedAt(new Date())
						.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
						.signWith(secretKey)
						.compact();
	}

	public String generateToken(Long userId, User.Role role, Date date) {
		return
				Jwts
						.builder()
						.subject(String.valueOf(userId))
						.claim("role", role.name())
						.issuedAt(new Date())
						.expiration(date)
						.signWith(secretKey)
						.compact();
	}

	// JWT 리프레시 토큰 생성
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

	// JWT 검증
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

	// JWT 에서 유저 정보 가져오기
	public Auth getUserInfoFromToken(String token) {
		Claims claims = jwtParser.parseSignedClaims(token).getPayload();
		Long userId = Long.valueOf(claims.getSubject());

		String roleString = claims.get("role", String.class);
		User.Role role = User.Role.valueOf(roleString);

		return
				Auth
						.builder()
						.userId(userId)
						.role(role)
						.build();
	}

	// todo: 쿠키 방식이면 메소드 사용 안함
	// 요청 헤더에서 JWT 토큰 추출
	// public Optional<String> resolveToken(HttpServletRequest request) {
	// 	return
	// 		Optional
	// 			.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
	// 			.filter(token -> token.length() >= 7 && token.substring(0, 7).equalsIgnoreCase("Bearer "))
	// 			.map(token -> token.substring(7));
	// }

}

