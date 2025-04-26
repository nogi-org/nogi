package kr.co.nogibackend.global.config.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import kr.co.nogibackend.global.config.logging.p6spy.P6SpyEventListener;
import kr.co.nogibackend.global.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQLLoggingInterceptor implements HandlerInterceptor {

	@Value("${decorator.datasource.p6spy.enable-logging:true}")
	private boolean enableSQLLogging;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) {
		if (enableSQLLogging) {
			P6SpyEventListener.dmlCount.set(0);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler,
			Exception ex) {
		if (!enableSQLLogging) {
			return;
		}

		Integer dmlCount = P6SpyEventListener.dmlCount.get();
		if (dmlCount == null) {
			dmlCount = 0;
		}

		log.info(StringUtils.replaceVariables("""
				\n🟢 [DML Query Summary]
				🔹 URI: ${uri}
				🔹 Method: ${method}
				🔹 Total DML Queries Executed: ${dmlCount}
				----------------------------------------------------------------------------------------------------
				""", Map.of(
				"uri", request.getRequestURI(),
				"method", request.getMethod(),
				"dmlCount", String.valueOf(dmlCount)
		)));

		P6SpyEventListener.dmlCount.remove();
	}
}
