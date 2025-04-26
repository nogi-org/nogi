package kr.co.nogibackend.global.config.openfeign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import kr.co.nogibackend.global.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class NotionFeignClientConfig implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		if (template.url().contains("oauth/token")) {
			return;
		}

		template.header("Notion-Version", "2022-06-28");
		template.header("Content-Type", "application/json; charset=UTF-8");
		var newHeaders = HttpRequestUtils.handleBearerToken(template.headers());
		// 기존 헤더 초기화 후 새 헤더 설정(초기화 되지 않는 문제가 있어서 초기화 후 설정)
		template.headers(null);
		template.headers(newHeaders);
	}

	// todo: 에러 핸들링 필요시 사용하기
	// @Override
	// public Exception decode(String methodKey, Response response) {
	// 	return response.;
	// }
	@Bean
	public Logger feignLogger() {
		return new CustomFeignLogger();
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL; // FULL로 해야 Body까지 출력
	}

}
