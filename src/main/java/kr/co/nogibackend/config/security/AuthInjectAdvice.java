package kr.co.nogibackend.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/*
  Package Name : kr.co.nogibackend.config.security
  File Name    : AuthInjectAdvice
  Author       : superpil
  Created Date : 25. 2. 14.
  Description  : 모든 Controller 에 Auth 객체가 있다면 현재 로그인 유저 정보 자동 주입
 */
@ControllerAdvice
public class AuthInjectAdvice {

	@ModelAttribute
	public Auth injectAuth() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 현재 로그인한 사용자 정보 반환
		if (authentication != null && authentication.getPrincipal() instanceof Auth auth) {
			return auth;
		}

		// 로그인하지 않은 경우 null 반환
		return null;
	}
}
