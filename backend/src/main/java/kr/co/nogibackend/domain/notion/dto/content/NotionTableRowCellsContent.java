package kr.co.nogibackend.domain.notion.dto.content;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionTableRowCellsContent {

  private String type;
  private NotionTableCellTextContent text;
  private NotionAnnotationsContent annotations;
  private String plain_text;
  private String href;

}
