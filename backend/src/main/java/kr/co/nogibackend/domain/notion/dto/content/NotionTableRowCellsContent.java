package kr.co.nogibackend.domain.notion.dto.content;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionTableRowCellsContent {

	private String type;
	private String plain_text;
	private String href;
	private NotionTableCellTextContent text;
	private NotionAnnotationsContent annotations;

}
