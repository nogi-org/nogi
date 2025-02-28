package kr.co.nogibackend.config.security;

import kr.co.nogibackend.config.audit.AuditContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AuthInjectAdvice {

  @ModelAttribute
  public Auth injectAuth() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // 현재 로그인한 사용자 정보 반환
    if (authentication != null && authentication.getPrincipal() instanceof Auth auth) {
      AuditContext.setUserId(auth.getUserId());
      return auth;
    }

    // 로그인하지 않은 경우 null 반환
    AuditContext.setUserId(0L);
    return null;
  }
}
