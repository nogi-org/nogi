package kr.co.nogibackend.domain.admin.dto.response;


import java.util.List;
import java.util.Map;

public record NotionPageIdUpdateResponse(
		List<String> success,
		List<String> fail
) {

	public static NotionPageIdUpdateResponse from(Map<String, List<String>> result) {
		return new NotionPageIdUpdateResponse(result.get("success"), result.get("fail"));
	}

}
