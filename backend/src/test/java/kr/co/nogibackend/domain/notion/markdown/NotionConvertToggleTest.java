package kr.co.nogibackend.domain.notion.markdown;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import kr.co.nogibackend.domain.notion.content.NotionToggleBlocksContent;
import kr.co.nogibackend.domain.notion.result.CompletedPageMarkdownResult.ImageOfNotionBlock;
import kr.co.nogibackend.domain.notion.helper.NotionMarkdownConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotionConvertToggleTest {

  static List<NotionToggleBlocksContent> contents = new ArrayList<>();
  @InjectMocks
  private NotionMarkdownConverter notionMarkdownConverter;

  @BeforeAll
  static void setUp() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    InputStream is =
        NotionToggleBlocksContent.class
            .getClassLoader()
            .getResourceAsStream("json/NotionToggleBlockData.json");

    contents =
        mapper.readValue(is, new TypeReference<>() {
        });
  }

  @Test
  @DisplayName("토글버튼 마크다운")
  void convert1() {
    List<ImageOfNotionBlock> images = new ArrayList<>();
    String markdown =
        notionMarkdownConverter.buildToggle(contents.get(0), "gitowner", images);
    System.out.println(markdown);
  }

}
