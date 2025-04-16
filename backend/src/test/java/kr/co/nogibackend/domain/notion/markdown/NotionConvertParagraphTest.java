package kr.co.nogibackend.domain.notion.markdown;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.domain.notion.dto.content.NotionParagraphContent;
import kr.co.nogibackend.domain.notion.helper.NotionMarkdownConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotionConvertParagraphTest {

  static List<NotionParagraphContent> contents = new ArrayList<>();
  @InjectMocks
  private NotionMarkdownConverter notionMarkdownConverter;

  @BeforeAll
  static void setUp() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    InputStream is =
        NotionParagraphContent.class
            .getClassLoader()
            .getResourceAsStream("json/NotionParagraphBlockData.json");

    contents =
        mapper.readValue(is, new TypeReference<>() {
        });
  }

  @Test
  @DisplayName("단순 문장")
  void convert1() {
    String markdown =
        notionMarkdownConverter.buildParagraph(contents.get(0));
    System.out.println(markdown);
  }

  @Test
  @DisplayName("shift enter 문장")
  void convert2() {
    String markdown =
        notionMarkdownConverter.buildParagraph(contents.get(1));
    System.out.println(markdown);
  }

  @Test
  @DisplayName("shift enter 첫번째 문장에 링크")
  void convert3() {
    String markdown =
        notionMarkdownConverter.buildParagraph(contents.get(2));
    System.out.println(markdown);
  }

  @Test
  @DisplayName("문장 어노데이션 종합")
  void convert4() {
    String markdown =
        notionMarkdownConverter.buildParagraph(contents.get(3));
    System.out.println(markdown);
  }

}
