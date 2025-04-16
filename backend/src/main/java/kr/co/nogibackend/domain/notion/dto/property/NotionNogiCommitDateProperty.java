package kr.co.nogibackend.domain.notion.dto.property;

import kr.co.nogibackend.util.DateUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionNogiCommitDateProperty extends NotionNogiCommonProperty {

  private NotionDateProperty date;

  public static NotionNogiCommitDateProperty buildTodayDateAsYYYYMMDDString() {
    NotionDateProperty notionDateProperty = new NotionDateProperty();
    notionDateProperty.setStart(DateUtils.getTodayDateAsYYYYMMDDString());

    NotionNogiCommitDateProperty notionNogiCommitDateProperty = new NotionNogiCommitDateProperty();
    notionNogiCommitDateProperty.setDate(notionDateProperty);

    return notionNogiCommitDateProperty;
  }

}
