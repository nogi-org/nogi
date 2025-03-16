package kr.co.nogibackend.domain.notion.dto.content;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionTableContent {

	private String table_width;
	private boolean has_column_header;
	private boolean has_row_header;
}
