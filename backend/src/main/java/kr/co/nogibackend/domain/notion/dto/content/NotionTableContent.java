package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionTableContent {

	private String table_width;
	private boolean has_column_header;
	private boolean has_row_header;
	
	// notion에서 데이터 받았을때 주입되지 않음, 마크다운 전처리 로직에서 넣어줌
	private List<NotionTableRowContent> rows;
}
