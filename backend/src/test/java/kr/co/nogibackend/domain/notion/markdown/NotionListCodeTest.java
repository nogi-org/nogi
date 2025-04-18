package kr.co.nogibackend.domain.notion.markdown;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.domain.notion.content.NotionListItemContent;
import kr.co.nogibackend.domain.notion.helper.NotionMarkdownConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotionListCodeTest {

  static List<NotionListItemContent> contents = new ArrayList<>();
  @InjectMocks
  private NotionMarkdownConverter notionMarkdownConverter;

  @BeforeAll
  static void setUp() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    InputStream is =
        NotionListItemContent.class
            .getClassLoader()
            .getResourceAsStream("json/NotionListItemBlockData.json");

    contents =
        mapper.readValue(is, new TypeReference<>() {
        });
  }

  @Test
  @DisplayName("숫자없는 리스트1에 1뎁스 자식요소 포함")
  void convert1() {
    String markdown =
        notionMarkdownConverter.buildListItem(contents.get(0), false);
    System.out.println(markdown);
  }

  @Test
  @DisplayName("숫자있는 리스트1에 1뎁스 자식요소 포함")
  void convert2() {
    String markdown =
        notionMarkdownConverter.buildListItem(contents.get(1), true);
    System.out.println(markdown);
  }

  @Test
  @DisplayName("숫자있는 리스트1에 2뎁스 자식요소 포함")
  void convert3() {
    String markdown =
        notionMarkdownConverter.buildListItem(contents.get(2), true);
    System.out.println(markdown);
  }

}
