package kr.co.nogibackend.domain.notion.dto.content;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
  Package Name : kr.co.nogibackend.interfaces.notion.response.content
  File Name    : BookmarkContent
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Getter
@Setter
@ToString
public class NotionBookmarkContent {

	private List<NotionRichTextContent> caption;
	private String url;

}
