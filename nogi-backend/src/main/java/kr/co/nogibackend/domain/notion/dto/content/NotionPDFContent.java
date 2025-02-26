package kr.co.nogibackend.domain.notion.dto.content;

import kr.co.nogibackend.domain.notion.dto.property.NotionFileProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
  Package Name : kr.co.nogibackend.interfaces.notion.response.content
  File Name    : NotionPDFContent
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Getter
@Setter
@ToString
public class NotionPDFContent {

  private String type;
  private NotionFileProperty external;

}
