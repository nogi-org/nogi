package kr.co.nogibackend.domain.notion.dto.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/*
reference:
https://developers.notion.com/reference/rich-text#text
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionTextContent {

  private String content;
  private NotionLinkContent link;

  public static NotionTextContent buildContent(String content) {
    NotionTextContent notionTextContent = new NotionTextContent();
    notionTextContent.setContent(content);

    return notionTextContent;
  }

  public static NotionTextContent buildLinkContent(String content, String url) {
    NotionTextContent notionTextContent = new NotionTextContent();
    notionTextContent.setContent(content);

    NotionLinkContent notionLinkContent = new NotionLinkContent();
    notionLinkContent.setUrl(url);
    notionTextContent.setLink(notionLinkContent);

    return notionTextContent;
  }

}
