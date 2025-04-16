package kr.co.nogibackend.domain.notion.dto.property;

import kr.co.nogibackend.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotionNogiCommitDateProperty extends NotionNogiCommonProperty {

  private NotionDateProperty date;

  public static NotionNogiCommitDateProperty buildTodayDateAsYYYYMMDDString() {
    return
        new NotionNogiCommitDateProperty(
            new NotionDateProperty(DateUtils.getTodayDateAsYYYYMMDDString())
        );
  }

}
