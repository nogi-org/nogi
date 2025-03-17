package kr.co.nogibackend.domain.notion.markdown;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.co.nogibackend.domain.notion.NotionService;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableRowCellsContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableRowContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotionConvertMarkdownTable {

  final String textCellContent = "{\n"
      + "                            \"type\": \"text\",\n"
      + "                            \"text\": {\n"
      + "                                \"content\": \"1\",\n"
      + "                                \"link\": null\n"
      + "                            },\n"
      + "                            \"plain_text\": \"superpil\",\n"
      + "                            \"href\": null\n"
      + "                        }";

  final String linkCellContent = "{\n"
      + "                            \"type\": \"text\",\n"
      + "                            \"text\": {\n"
      + "                                \"content\": \"https://naver.com\",\n"
      + "                                \"link\": {\n"
      + "                                    \"url\": \"https://naver.com/\"\n"
      + "                                }\n"
      + "                            },\n"
      + "                            \"plain_text\": \"https://naver.com\",\n"
      + "                            \"href\": \"https://naver.com/\"\n"
      + "                        }";

  @InjectMocks
  private NotionService notionService;

  @Test
  void convertMarkdownTable() throws JsonProcessingException {
    // cell
    ObjectMapper objectMapper = new ObjectMapper();
    NotionTableRowCellsContent cell1 =
        objectMapper.readValue(textCellContent, NotionTableRowCellsContent.class);
    NotionTableRowCellsContent cell2 =
        objectMapper.readValue(linkCellContent, NotionTableRowCellsContent.class);
    NotionTableRowCellsContent cell3 =
        objectMapper.readValue(linkCellContent, NotionTableRowCellsContent.class);

    // row
    NotionTableRowContent row = new NotionTableRowContent();
    row.setCells(List.of(cell1, cell2, cell3));

    // rows
    NotionBlockInfo rows = new NotionBlockInfo();
    rows.setTable_row(row);

    // when
    String markdown = notionService.convertMarkdownByTable(List.of(rows));
    System.out.println(markdown);
  }

}
