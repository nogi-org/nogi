package kr.co.nogibackend.domain.notion.markdown;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.domain.notion.content.NotionTableContent;
import kr.co.nogibackend.domain.notion.helper.NotionMarkdownConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotionConvertTableTest {

  static List<NotionTableContent> contents = new ArrayList<>();
  @InjectMocks
  private NotionMarkdownConverter notionMarkdownConverter;

  @BeforeAll
  static void setUp() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    InputStream is =
        NotionTableContent.class
            .getClassLoader()
            .getResourceAsStream("json/NotionTableBlockData.json");

    contents =
        mapper.readValue(is, new TypeReference<>() {
        });
  }

  @Test
  @DisplayName("단순 테이블")
  void convert1() {
    String markdown =
        notionMarkdownConverter.buildTable(contents.get(0));
    System.out.println(markdown);
  }

  @Test
  @DisplayName("스타일 추가 테이블")
  void convert2() {
    String markdown =
        notionMarkdownConverter.buildTable(contents.get(1));
    System.out.println(markdown);
  }

}
