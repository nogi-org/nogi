package kr.co.nogibackend.global.config.openfeign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import kr.co.nogibackend.global.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GitHubFeignClientConfig implements RequestInterceptor {

	private static final String TOKEN_PREFIX = "Bearer ";
	private static final String AUTHORIZATION_HEADER = "Authorization";

	@Override
	public void apply(RequestTemplate template) {
		template.header("Accept", "application/vnd.github+json");
		template.header("Content-Type", "application/json");
		var newHeaders = HttpRequestUtils.handleBearerToken(template.headers());

		// 기존 헤더 초기화 후 새 헤더 설정(초기화 되지 않는 문제가 있어서 초기화 후 설정)
		template.headers(null);
		template.headers(newHeaders);
	}
}
