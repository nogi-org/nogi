package kr.co.nogibackend.config.security;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.nogibackend.config.security1.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
  Package Name : com.exitkrapi.config.security
  File Name    : SecurityTokenFilter
  Author       : superpil
  Created Date : 2/9/24
  Description  :
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityTokenFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	// spring security 필터
	@Override
	protected void doFilterInternal(
		HttpServletRequest request
		, HttpServletResponse response
		, FilterChain chain
	) throws ServletException, IOException {

		String token = jwtTokenProvider.resolveToken(request);
		boolean hasValidToken = jwtTokenProvider.validateToken(token);

		if (hasValidToken) {
			UUID externalId = jwtTokenProvider.parserUserExternalId(token);
			// todo: userId, role 를 담아서 아래 메소드에 넘기기
			Auth auth = this.findTokenUser(externalId, token);
			this.authenticateUser(auth);
		}

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
