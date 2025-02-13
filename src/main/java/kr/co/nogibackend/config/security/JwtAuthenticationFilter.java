package kr.co.nogibackend.config.security;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	// 인증 필터를 적용하지 않을 URL 목록
	private static final List<String> EXCLUDED_URLS = List.of(
		"/health-check",
		"/github/auth-url",
		"/login/code/github"
	);

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return EXCLUDED_URLS.contains(requestURI);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String token = jwtProvider.resolveToken(request);

		if (token == null || !jwtProvider.validateToken(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			// TODO 응답 형식 변경
			String jsonResponse = "{\"message\": \"Unauthorized: Invalid or missing token\"}";
			response.getWriter().write(jsonResponse);
			return;
		}

		// 인증 성공 시 SecurityContext 설정 (추가 가능)
		filterChain.doFilter(request, response);
	}

}
