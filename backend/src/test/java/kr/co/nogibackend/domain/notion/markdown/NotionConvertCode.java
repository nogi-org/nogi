package kr.co.nogibackend.domain.notion.markdown;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.domain.notion.dto.content.NotionCodeContent;
import kr.co.nogibackend.domain.notion.helper.NotionMarkdownConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotionConvertCode {

	@InjectMocks
	private NotionMarkdownConverter notionMarkdownConverter;

	static List<NotionCodeContent> contents = new ArrayList<>();

	@BeforeAll
	static void setUp() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		InputStream is =
				NotionCodeContent.class
						.getClassLoader()
						.getResourceAsStream("json/NotionCodeBlockData.json");

		contents =
				mapper.readValue(is, new TypeReference<>() {
				});
	}

	@Test
	@DisplayName("코드")
	void convert1() {
		String markdown =
				notionMarkdownConverter.buildCode(contents.get(0));
		System.out.println(markdown);
	}

}
