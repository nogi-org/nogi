package kr.co.nogibackend.domain.notion.markdown;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.domain.notion.dto.content.NotionTodoContent;
import kr.co.nogibackend.domain.notion.helper.NotionMarkdownConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotionTodoCode {

	@InjectMocks
	private NotionMarkdownConverter notionMarkdownConverter;

	static List<NotionTodoContent> contents = new ArrayList<>();

	@BeforeAll
	static void setUp() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		InputStream is =
				NotionTodoContent.class
						.getClassLoader()
						.getResourceAsStream("json/NotionTodoBlockData.json");

		contents =
				mapper.readValue(is, new TypeReference<>() {
				});
	}

	@Test
	@DisplayName("체크되지않은")
	void convert1() {
		String markdown =
				notionMarkdownConverter.buildTodo(contents.get(0));
		System.out.println(markdown);
	}

	@Test
	@DisplayName("체크된")
	void convert2() {
		String markdown =
				notionMarkdownConverter.buildTodo(contents.get(1));
		System.out.println(markdown);
	}

}
