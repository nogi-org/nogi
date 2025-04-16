package kr.co.nogibackend.domain.notion.dto.property;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionNogiTitleProperty extends NotionNogiCommonProperty {

  private List<NotionRichTextContent> title;

  public static NotionNogiTitleProperty of(List<String> titles) {

    List<NotionRichTextContent> richTexts =
        titles
            .stream()
            .map(NotionRichTextContent::buildText)
            .toList();

    NotionNogiTitleProperty notionNogiTitleProperty = new NotionNogiTitleProperty();
    notionNogiTitleProperty.setTitle(richTexts);

    return notionNogiTitleProperty;
  }

}
