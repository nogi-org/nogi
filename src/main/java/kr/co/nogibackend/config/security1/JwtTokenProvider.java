package kr.co.nogibackend.config.security1;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {

	private byte[] secretKeyDecode;
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.expiration-minute}")
	private int expirationMinute;

	/*
	 Description : secretKey encode(string to base64) jwt token 생성 및 해독을 위한 key인코딩
	 */
	@Override
	public void afterPropertiesSet() {
		secretKeyDecode = Decoders.BASE64.decode(secretKey);
	}

	/*
	 Description : request에서 token 가져오기
	 */
	public String resolveToken(HttpServletRequest request) {
		return
			Optional
				.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
				.filter(token -> token.length() >= 7 && token.substring(0, 7).equalsIgnoreCase("Bearer "))
				.map(token -> token.substring(7))
				.orElse(null);
	}

	/*
	 Description : token 유효시간 검사 && secretKey 검사
	 */
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = this.parseClaims(token);
			return
				!claims
					.getBody()
					.getExpiration()
					.before(new Date());
		} catch (Exception error) {
			return false;
		}
	}

	/*
	 Description : token body에서 유저외부ID 가져오기
	 */
	public UUID parserUserExternalId(String token) {
		Jws<Claims> claims = this.parseClaims(token);
		return UUID.fromString(claims.getBody().get("userExternalId", String.class));
	}

	/*
	 Description : 토큰 생성
	 */
	public String createToken(UUID userExternalId) {
		Date now = new Date();
		return
			Jwts
				.builder()
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + (long)expirationMinute * 60 * 1000))
				.signWith(Keys.hmacShaKeyFor(secretKeyDecode), SignatureAlgorithm.HS256)
				.compact();
	}

	/*
	 Description : 토큰생성에 담을 claims 생성
	 */
	// private Claims createClaims(UUID userExternalId) {
	// 	Claims claims =
	// 		Jwts
	// 			.claims()
	// 			.setSubject("NOGI");
	// 	claims.put("userExternalId", userExternalId);
	// 	return claims;
	// }

	// token 파싱
	private Jws<Claims> parseClaims(String token) {
		return
			Jwts
				.parser()
				.verifyWith(Keys.hmacShaKeyFor(secretKeyDecode))
				.build()
				.parseSignedClaims(token);
	}

}
