package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionToggleBlocksContent {

  private List<NotionRichTextContent> rich_text;
  private String color;
  private List<NotionBlockInfo> children;

}
