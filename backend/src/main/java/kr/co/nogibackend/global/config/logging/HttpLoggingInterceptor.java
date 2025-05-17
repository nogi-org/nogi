package kr.co.nogibackend.global.config.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import kr.co.nogibackend.global.config.logging.cachebody.CachedBodyHttpServletRequest;
import kr.co.nogibackend.global.config.logging.cachebody.CachedBodyHttpServletResponse;
import kr.co.nogibackend.global.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpLoggingInterceptor implements HandlerInterceptor {

	private final Map<String, Long> requestTimestamps = new ConcurrentHashMap<>();

	// ✅ 로그 제외할 URI 목록
	private final Set<String> excludedUris = Set.of(
			"/health-check",
			"/actuator/health"
	);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) {

		// ✅ 제외할 URI라면 로깅 안함
		if (excludedUris.contains(request.getRequestURI())) {
			return true;
		}

		requestTimestamps.put(getRequestKey(request), System.currentTimeMillis());

		if (request instanceof CachedBodyHttpServletRequest cachedRequest) {
			logRequest(cachedRequest);
		} else {
			log.warn(
					"⚠️ Request is not wrapped with CachedBodyHttpServletRequest. Ensure CacheBodyFilter is applied.");
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler,
			Exception ex) {
		// ✅ 제외할 URI라면 로깅 안함
		if (excludedUris.contains(request.getRequestURI())) {
			return;
		}

		long startTime = requestTimestamps.getOrDefault(getRequestKey(request),
				System.currentTimeMillis());
		long duration = System.currentTimeMillis() - startTime;

		if (response instanceof CachedBodyHttpServletResponse cachedResponse) {
			// todo: 수정 필요
			// logResponse(cachedResponse, duration);
		} else {
			log.warn(
					"⚠️ Response is not wrapped with CachedBodyHttpServletResponse. Ensure CacheBodyFilter is applied.");
		}

		requestTimestamps.remove(getRequestKey(request));
	}

	/**
	 * ✅ 요청(Request) 로깅
	 */
	private void logRequest(CachedBodyHttpServletRequest request) {
		Map<String, String> details = new HashMap<>();
		details.put("URI", request.getRequestURI());
		details.put("Method", request.getMethod());
		details.put("Headers", getHeaders(request).toString());
		details.put("QueryParams", getQueryParams(request).toString());

		byte[] requestBody = request.getCachedBody();
		if (requestBody.length == 0) {
			details.put("RequestBody", "(empty)");
		} else {
			details.put("RequestBody", StringUtils.formatJson(new String(requestBody)));
		}

		String logMessage = StringUtils.replaceVariables("""
				\n🟢 [REQUEST DETAILS]
				🔹 URI: ${URI}
				🔹 Method: ${Method}
				🔹 Headers: ${Headers}
				🔹 Query Params: ${QueryParams}
				🔹 Request Body: ${RequestBody}
				----------------------------------------------------------------------------------------------------
				""", details);

		log.info(logMessage);
	}

	/**
	 * ✅ 응답(Response) 로깅
	 */
	private void logResponse(CachedBodyHttpServletResponse response, long duration) {
		Map<String, String> details = new HashMap<>();
		details.put("Status", String.valueOf(response.getStatus()));
		details.put("Headers", getResponseHeaders(response).toString());
		details.put("Duration", duration + " ms");

		byte[] responseBody = response.getCachedBody();
		if (responseBody.length == 0) {
			details.put("ResponseBody", "(empty)");
		} else if (StringUtils.isJWT(new String(responseBody))) {
			details.put("ResponseBody", "[JWT Token]");
		} else {
			details.put("ResponseBody", StringUtils.formatJson(new String(responseBody)));
		}

		String logMessage = StringUtils.replaceVariables("""
				\n🟢 [RESPONSE DETAILS]
				🔹 Status: ${Status}
				🔹 Headers: ${Headers}
				🔹 Duration: ${Duration}
				🔹 Response Body: ${ResponseBody}
				----------------------------------------------------------------------------------------------------
				""", details);

		log.info(logMessage);
	}

	private Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> headers = new HashMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headers.put(headerName, request.getHeader(headerName));
		}
		return headers;
	}

	private Map<String, String> getQueryParams(HttpServletRequest request) {
		Map<String, String> queryParams = new HashMap<>();
		request.getParameterMap()
				.forEach((key, value) -> queryParams.put(key, String.join(",", value)));
		return queryParams;
	}

	private Map<String, String> getResponseHeaders(CachedBodyHttpServletResponse response) {
		Map<String, String> headers = new HashMap<>();
		for (String headerName : response.getHeaderNames()) {
			headers.put(headerName, response.getHeader(headerName));
		}
		return headers;
	}

	private String getRequestKey(HttpServletRequest request) {
		return request.getMethod() + ":" + request.getRequestURI();
	}
}
