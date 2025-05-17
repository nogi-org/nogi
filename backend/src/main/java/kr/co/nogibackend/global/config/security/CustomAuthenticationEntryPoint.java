package kr.co.nogibackend.global.config.security;

import static kr.co.nogibackend.global.response.code.UserResponseCode.F_401;
import static kr.co.nogibackend.global.util.CookieUtils.ACCESS_COOKIE_NAME;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.nogibackend.global.response.service.Response;
import kr.co.nogibackend.global.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final CookieUtils cookieUtil;

	// 401 에러처리
	@Override
	public void commence(
			HttpServletRequest request
			, HttpServletResponse response
			, AuthenticationException authException
	) throws IOException, ServletException {
		cookieUtil.deleteCookie(response, ACCESS_COOKIE_NAME);
		ResponseEntity<?> failResult = Response.fail(F_401);
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(new ObjectMapper().writeValueAsString(failResult.getBody()));
	}

}
