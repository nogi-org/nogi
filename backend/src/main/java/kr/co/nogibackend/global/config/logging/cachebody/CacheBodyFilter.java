package kr.co.nogibackend.global.config.logging.cachebody;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class CacheBodyFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {

		CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
		CachedBodyHttpServletResponse cachedResponse = new CachedBodyHttpServletResponse(response);

		try {
			filterChain.doFilter(cachedRequest, cachedResponse);
		} finally {
			cachedResponse.copyBodyToResponse(); // 응답을 클라이언트에게 전달
		}
	}
}
