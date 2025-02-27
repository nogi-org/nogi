package kr.co.nogibackend.domain.notion.dto.property;

import java.util.List;
import kr.co.nogibackend.domain.notion.dto.content.NotionRichTextContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotionNogiCommitMessageProperty extends NotionNogiCommonProperty {

  private List<NotionRichTextContent> rich_text;

}
