package kr.co.nogibackend.config.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public FilterConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration() {
		FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(jwtAuthenticationFilter);
		registrationBean.addUrlPatterns("/*"); // 모든 요청에 대해 필터 적용
		registrationBean.setOrder(1); // 필터 실행 순서 (낮을수록 먼저 실행)

		return registrationBean;
	}
}
