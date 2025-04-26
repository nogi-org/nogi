package kr.co.nogibackend.global.config.security;

import static kr.co.nogibackend.global.response.code.UserResponseCode.F_403;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.nogibackend.global.response.service.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	// 403 에러처리
	@Override
	public void handle(
			HttpServletRequest request
			, HttpServletResponse response
			, AccessDeniedException accessDeniedException
	) throws IOException, ServletException {
		ResponseEntity<?> failResult = Response.fail(F_403);
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write(new ObjectMapper().writeValueAsString(failResult.getBody()));
	}

}
