package kr.co.nogibackend.domain.notion.content;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionTableCellTextContent {

  private String content;
  private NotionLinkContent link;
}
