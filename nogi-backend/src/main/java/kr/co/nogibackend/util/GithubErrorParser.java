package kr.co.nogibackend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;

public class GithubErrorParser {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String extractErrorMessage(FeignException e) {
		try {
			String responseBody = e.contentUTF8();
			JsonNode jsonNode = objectMapper.readTree(responseBody);

			// 1️⃣ 최상위 "message" 필드 가져오기
			String mainMessage = jsonNode.has("message") ? jsonNode.get("message").asText() : "Unknown error";

			// 2️⃣ "errors" 배열이 있는 경우 추가 메시지 파싱
			if (jsonNode.has("errors") && jsonNode.get("errors").isArray()) {
				StringBuilder errorMessages = new StringBuilder(mainMessage);
				for (JsonNode error : jsonNode.get("errors")) {
					if (error.has("message")) {
						errorMessages.append(" - ").append(error.get("message").asText());
					}
				}
				return errorMessages.toString();
			}

			return mainMessage;
		} catch (Exception ex) {
			return "Failed to parse error response";
		}
	}
}
