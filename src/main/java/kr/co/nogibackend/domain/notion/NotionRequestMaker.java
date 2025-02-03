package kr.co.nogibackend.domain.notion;

import static kr.co.nogibackend.domain.notion.NotionPropertyKey.*;

import java.util.Map;

public class NotionRequestMaker {

	/*
	필터 구조 예제
	{
	  "filter": {
		"property": "Task completed",
		"checkbox": {
		  "does_not_equal": true
		}
	  }
	}
   */
	public static Map<String, Object> filterStatusEq(NotionPropertyValue status) {
		Map<String, Object> value =
			Map.of(
				"property", STATUS.getName()
				, "select", Map.of("equals", status.getName()));
		return filter(value);
	}

	public static Map<String, Object> filter(Map<String, Object> value) {
		return Map.of("filter", value);
	}

	public static Map<String, Object> properties(Map<String, Object> value) {
		return Map.of("properties", value);
	}

}
