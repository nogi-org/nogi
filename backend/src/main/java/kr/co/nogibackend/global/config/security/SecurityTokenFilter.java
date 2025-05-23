package kr.co.nogibackend.global.config.security;

import static kr.co.nogibackend.global.util.CookieUtils.ACCESS_COOKIE_NAME;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import kr.co.nogibackend.global.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityTokenFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	// spring security 필터
	@Override
	protected void doFilterInternal(
			HttpServletRequest request
			, HttpServletResponse response
			, FilterChain chain
	) throws ServletException, IOException {

		CookieUtils
				.getCookie(request, ACCESS_COOKIE_NAME)
				.map(Cookie::getValue)
				.filter(jwtProvider::validateToken)
				.map(jwtProvider::getUserInfoFromToken)
				.ifPresent(this::authenticateUser);

		chain.doFilter(request, response);
	}

	// SecurityContextHolder 설정
	private void authenticateUser(Auth auth) {
		List<GrantedAuthority> grantedAuthorities =
				auth == null ? null : auth.toSimpleGrantedAuthority();

		Authentication authentication =
				new UsernamePasswordAuthenticationToken(auth, null, grantedAuthorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
