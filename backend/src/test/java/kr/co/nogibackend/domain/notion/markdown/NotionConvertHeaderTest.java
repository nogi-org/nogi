package kr.co.nogibackend.domain.notion.markdown;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.domain.notion.content.NotionHeadingContent;
import kr.co.nogibackend.domain.notion.helper.NotionMarkdownConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotionConvertHeaderTest {

  static List<NotionHeadingContent> contents = new ArrayList<>();
  @InjectMocks
  private NotionMarkdownConverter notionMarkdownConverter;

  @BeforeAll
  static void setUp() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    InputStream is =
        NotionHeadingContent.class
            .getClassLoader()
            .getResourceAsStream("json/NotionHeaderBlockData.json");

    contents =
        mapper.readValue(is, new TypeReference<>() {
        });
  }

  @Test
  @DisplayName("단순 해더1")
  void convert1() {
    String markdown =
        notionMarkdownConverter.buildHeading(1, contents.get(0));
    System.out.println(markdown);
  }

  @Test
  @DisplayName("두줄 해더1")
  void convert2() {
    String markdown =
        notionMarkdownConverter.buildHeading(1, contents.get(1));
    System.out.println(markdown);
  }

  @Test
  @DisplayName("두줄 스타일 해더1")
  void convert3() {
    String markdown =
        notionMarkdownConverter.buildHeading(1, contents.get(2));
    System.out.println(markdown);
  }

  @Test
  @DisplayName("링크 해더1")
  void convert4() {
    String markdown =
        notionMarkdownConverter.buildHeading(1, contents.get(3));
    System.out.println(markdown);
  }

  @Test
  @DisplayName("단순 헤더2")
  void convert5() {
    String markdown =
        notionMarkdownConverter.buildHeading(2, contents.get(0));
    System.out.println(markdown);
  }

  @Test
  @DisplayName("단순 헤더3")
  void convert6() {
    String markdown =
        notionMarkdownConverter.buildHeading(3, contents.get(0));
    System.out.println(markdown);
  }

}
