package kr.co.nogibackend.domain.notion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kr.co.nogibackend.domain.notion.service.NotionDatabaseService;
import kr.co.nogibackend.domain.notion.service.NotionPageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class NotionDemoTest {

	@Autowired
	private NotionPageService notionPageService;
	@Autowired
	private NotionDatabaseService notionDatabaseService;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("데모2")
	void test2() throws JsonProcessingException {
		String property = "{\n"
				+ "  \"properties\": {\n"
				+ "    \"nogiFM\": {\n"
				+ "      \"rich_text\": {}\n"
				+ "    }\n"
				+ "  }\n"
				+ "}";
		Map<String, Object> result = objectMapper.readValue(property,
				new TypeReference<Map<String, Object>>() {
				});
		System.out.println("result  ===>> " + result);
		// System.out.println("결과!! -> " + notionDatabaseService.createProperty(result));
	}

	@Test
	@DisplayName("데모1")
	void test1() throws JsonProcessingException {
		System.out.println("result = " + notionDatabaseService.getDatabase(29L));
	}

}
