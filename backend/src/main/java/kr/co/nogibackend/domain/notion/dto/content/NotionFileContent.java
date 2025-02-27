package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.property.NotionFileProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
  Package Name : kr.co.nogibackend.interfaces.notion.response.content
  File Name    : NotionFileContent
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Getter
@Setter
@ToString
public class NotionFileContent {

  private List<NotionRichTextContent> caption;
  private String type;
  private NotionFileProperty external;
  private String name;

}
