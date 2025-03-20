package kr.co.nogibackend.domain.notion.markdown;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableRowCellsContent;
import kr.co.nogibackend.domain.notion.dto.content.NotionTableRowContent;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.service.NotionService;
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
		NotionTableRowContent row1 = new NotionTableRowContent();
		row1.setCells(List.of(cell1, cell2, cell3));

		NotionTableRowContent row2 = new NotionTableRowContent();
		row2.setCells(List.of(cell3, cell2, cell1));

		// rows
		NotionBlockInfo rows1 = new NotionBlockInfo();
		rows1.setTable_row(row1);

		NotionBlockInfo rows2 = new NotionBlockInfo();
		rows2.setTable_row(row2);

		// when
		String markdown = notionService.convertMarkdownByTable(List.of(rows1, rows2));
		System.out.println(markdown);
	}

}
