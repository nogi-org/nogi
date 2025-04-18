package kr.co.nogibackend.domain.notion.content;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionTableRowCellsContent {

  private String type;
  private NotionTextContent text;
  private NotionAnnotationsContent annotations;
  private String plain_text;
  private String href;

}
