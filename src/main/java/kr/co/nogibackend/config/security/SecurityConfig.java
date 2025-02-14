package kr.co.nogibackend.config.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kr.co.nogibackend.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final SecurityTokenFilter securityTokenFilter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	@Value("${cors-config.allowed-origin}")
	private String allowedOrigin;

	// 권한(Role)별 URL 매핑
	private static final Map<String, Map<HttpMethod, String>> ROLE_PERMISSIONS = new HashMap<>();

	static {
		// ADMIN 권한 설정
		Map<HttpMethod, String> ADMIN_URL =
			Map.of(
				HttpMethod.POST, "/guide",
				HttpMethod.PUT, "/guide",
				HttpMethod.DELETE, "/guide"
			);

		// USER 권한 설정
		Map<HttpMethod, String> USER_URL =
			Map.of(
				HttpMethod.GET, "/guide/list"
			);

		ROLE_PERMISSIONS.put(User.Role.ADMIN.name(), ADMIN_URL);
		ROLE_PERMISSIONS.put(User.Role.USER.name(), USER_URL);
	}

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors((cors) -> cors.configurationSource(apiConfigurationSource()))
			.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.exceptionHandling((exceptionHandling) ->
				exceptionHandling
					.authenticationEntryPoint(customAuthenticationEntryPoint)
					.accessDeniedHandler(customAccessDeniedHandler)
			)
			.authorizeHttpRequests(authorizeRequests -> {
				ROLE_PERMISSIONS.forEach((role, methodUrls) ->
					methodUrls.forEach((method, url) ->
						authorizeRequests.requestMatchers(method, url).hasAuthority(role)
					)
				);
				// 그 외 모든 요청은 허용
				authorizeRequests.anyRequest().permitAll();
			})
			.addFilterBefore(securityTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	// 비밀번호 인코딩
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// role 상속 레벨 정의
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		// ADMIN > USER 계층 설정
		roleHierarchy.setHierarchy(User.Role.ADMIN.name() + " > " + User.Role.USER.name());
		return roleHierarchy;
	}

	// CORS 설정
	private CorsConfigurationSource apiConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		List<String> origins = List.of(this.allowedOrigin);
		configuration.addAllowedHeader("*");
		configuration.setAllowedOrigins(origins);
		configuration.setAllowCredentials(true);
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
