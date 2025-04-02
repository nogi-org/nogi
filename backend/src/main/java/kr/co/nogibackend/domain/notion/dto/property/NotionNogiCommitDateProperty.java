package kr.co.nogibackend.domain.notion.dto.property;

import kr.co.nogibackend.util.DateUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotionNogiCommitDateProperty extends NotionNogiCommonProperty {

	private NotionDateProperty date;

	public static NotionNogiCommitDateProperty buildTodayDateAsYYYYMMDDString() {
		return
				NotionNogiCommitDateProperty
						.builder()
						.date(new NotionDateProperty(DateUtils.getTodayDateAsYYYYMMDDString()))
						.build();
	}

}
