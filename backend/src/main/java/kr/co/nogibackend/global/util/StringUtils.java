package kr.co.nogibackend.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtils {

	/**
	 * 템플릿 문자열에서 ${key} 형태의 변수를 values에 있는 값으로 치환
	 *
	 * @param template 템플릿 문자열
	 * @param values   치환할 키-값 Map
	 * @return 변환된 문자열
	 */
	public static String replaceVariables(String template, Map<String, String> values) {
		if (template == null || values == null || values.isEmpty()) {
			return template;
		}
		for (Map.Entry<String, String> entry : values.entrySet()) {
			template = template.replace("${" + entry.getKey() + "}", entry.getValue());
		}
		return template;
	}

	/**
	 * ✅ JSON 포맷팅
	 */
	public static String formatJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Object jsonObj = mapper.readValue(json, Object.class);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
		} catch (Exception e) {
			log.warn("⚠️ Failed to format JSON properly. Returning raw JSON.");
			return json;
		}
	}

	/**
	 * ✅ JWT 형식인지 확인
	 */
	public static boolean isJWT(String responseBody) {
		return responseBody.matches("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$");
	}

}
