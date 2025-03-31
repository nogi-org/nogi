package kr.co.nogibackend.domain.notion.dto.property;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionNogiCommitDateProperty extends NotionNogiCommonProperty {

  private NotionDateProperty date;

}
