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

	// 권한(Role)별 URL 매핑
	private static final Map<String, Map<HttpMethod, List<String>>> ROLE_PERMISSIONS = new HashMap<>();

	static {
		// ADMIN 권한 설정
		Map<HttpMethod, List<String>> ADMIN_URL =
			Map.ofEntries(
				Map.entry(HttpMethod.POST, List.of("/guides")),
				Map.entry(HttpMethod.PUT, List.of("/guides")),
				Map.entry(HttpMethod.DELETE, List.of("/guides"))
			);

		// USER 권한 설정
		Map<HttpMethod, List<String>> USER_URL =
			Map.ofEntries(
				Map.entry(HttpMethod.GET, List.of("/users", "/users/{id}/validate-repository-name")),
				Map.entry(HttpMethod.PATCH, List.of("/users/{id}")),
				Map.entry(HttpMethod.POST, List.of("/users/manual-nogi"))
			);

		ROLE_PERMISSIONS.put(User.Role.ADMIN.name(), ADMIN_URL);
		ROLE_PERMISSIONS.put(User.Role.USER.name(), USER_URL);
	}

	private final SecurityTokenFilter securityTokenFilter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	@Value("${cors-config.allowed-origin}")
	private List<String> allowedOrigin;

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
					methodUrls.forEach((method, urls) ->
						urls.forEach(url -> authorizeRequests.requestMatchers(method, url).hasAuthority(role))
					)
				);
				// 그 외 모든 요청은 허용
				authorizeRequests.anyRequest().permitAll();
			})
			.logout(AbstractHttpConfigurer::disable)
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
		configuration.addAllowedHeader("*");
		configuration.setAllowedOrigins(this.allowedOrigin);
		configuration.setAllowCredentials(true);
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS", "PATCH"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
