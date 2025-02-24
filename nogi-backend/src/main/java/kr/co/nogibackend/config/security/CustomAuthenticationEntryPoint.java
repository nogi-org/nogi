package kr.co.nogibackend.config.security;

import static kr.co.nogibackend.response.code.UserResponseCode.*;
import static kr.co.nogibackend.util.CookieUtil.*;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.nogibackend.response.service.Response;
import kr.co.nogibackend.util.CookieUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final CookieUtil cookieUtil;

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
