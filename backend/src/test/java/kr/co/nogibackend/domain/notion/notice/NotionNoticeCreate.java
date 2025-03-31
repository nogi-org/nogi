package kr.co.nogibackend.domain.notion.notice;

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
public class NotionNoticeCreate {

  static List<NotionTodoContent> contents = new ArrayList<>();
  @InjectMocks
  private NotionMarkdownConverter notionMarkdownConverter;

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
  @DisplayName("노션에 신규 공지글 등록")
  void convert1() {
  }

}
