package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionTableRowContent {

  private List<List<NotionRichTextContent>> cells;

}
