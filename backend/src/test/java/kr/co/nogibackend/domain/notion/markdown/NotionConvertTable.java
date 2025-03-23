package kr.co.nogibackend.domain.notion.markdown;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotionConvertTable {

//  final String textCellContent = "{\n"
//      + "                            \"type\": \"text\",\n"
//      + "                            \"text\": {\n"
//      + "                                \"content\": \"1\",\n"
//      + "                                \"link\": null\n"
//      + "                            },\n"
//      + "                            \"plain_text\": \"superpil\",\n"
//      + "                            \"href\": null\n"
//      + "                        }";
//
//  final String linkCellContent = "{\n"
//      + "                            \"type\": \"text\",\n"
//      + "                            \"text\": {\n"
//      + "                                \"content\": \"https://naver.com\",\n"
//      + "                                \"link\": {\n"
//      + "                                    \"url\": \"https://naver.com/\"\n"
//      + "                                }\n"
//      + "                            },\n"
//      + "                            \"plain_text\": \"https://naver.com\",\n"
//      + "                            \"href\": \"https://naver.com/\"\n"
//      + "                        }";
//
//  @InjectMocks
//  private NotionWriteService notionWriteService;
//
//  @Test
//  void convertMarkdownTable() throws JsonProcessingException {
//    // cell
//    ObjectMapper objectMapper = new ObjectMapper();
//    NotionTableRowCellsContent cell1 =
//        objectMapper.readValue(textCellContent, NotionTableRowCellsContent.class);
//    NotionTableRowCellsContent cell2 =
//        objectMapper.readValue(linkCellContent, NotionTableRowCellsContent.class);
//    NotionTableRowCellsContent cell3 =
//        objectMapper.readValue(linkCellContent, NotionTableRowCellsContent.class);
//
//    // row
//    NotionTableRowContent row1 = new NotionTableRowContent();
//    row1.setCells(List.of(cell1, cell2, cell3));
//
//    NotionTableRowContent row2 = new NotionTableRowContent();
//    row2.setCells(List.of(cell3, cell2, cell1));
//
//    // rows
//    NotionBlockInfo rows1 = new NotionBlockInfo();
//    rows1.setTable_row(row1);
//
//    NotionBlockInfo rows2 = new NotionBlockInfo();
//    rows2.setTable_row(row2);
//
//    // when
//    String markdown = notionWriteService.convertMarkdownByTable(List.of(rows1, rows2));
//    System.out.println(markdown);
//  }

}
