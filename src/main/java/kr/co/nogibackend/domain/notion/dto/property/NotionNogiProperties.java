package kr.co.nogibackend.domain.notion.dto.property;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
reference:
https://developers.notion.com/reference/property-object#date
 */
@Getter
@Setter
@ToString
public class NotionNogiProperties {

	private NotionNogiCategoryProperty nogiCategory;
	private NotionNogiCommitDateProperty nogiCommitDate;
	private NotionNogiStatusProperty nogiStatus;
	private NotionNogiTitleProperty nogiTitle;

	public String getCategory() {
		return nogiCategory.getSelect().getName();
	}

	// todo: 공통 로직 메소드 분리, 노션에서 받은 날짜를 iso로 바꾸면 한국시랑이랑 달라지는지 체크
	public void createCommitDateWithCurrentTime() {
		LocalDateTime now = LocalDateTime.now();
		ZoneId koreaZone = ZoneId.of("Asia/Seoul");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
		String nowString = now.atZone(koreaZone).format(dateTimeFormatter);

		// 커밋 날짜가 없을 경우
		if (this.getNogiCommitDate().getDate() == null) {
			this.nogiCommitDate.setDate(new NotionDateProperty(nowString));
			return;
		}

		try {
			// 날짜와 시간 모두 있는 경우
			LocalDateTime parsedDateTime =
				LocalDateTime.parse(this.nogiCommitDate.getDate().getStart(), dateTimeFormatter);
			ZonedDateTime zonedDateTime = parsedDateTime.atZone(koreaZone);
			String format = zonedDateTime.format(isoFormatter);
			this.nogiCommitDate.getDate().setStart(format);
		} catch (DateTimeParseException error) {
			// 날짜만 있고 시간이 없는 경우
			LocalDate parsedDate =
				LocalDate.parse(this.nogiCommitDate.getDate().getStart(), dateFormatter);
			LocalDateTime nowKoreaTime = LocalDateTime.now(koreaZone);
			LocalDateTime localDateTime = parsedDate.atTime(nowKoreaTime.toLocalTime());
			ZonedDateTime zonedDateTime = localDateTime.atZone(koreaZone).withZoneSameInstant(ZoneId.of("UTC"));
			String isoDateTime = zonedDateTime.format(DateTimeFormatter.ISO_INSTANT);
			this.nogiCommitDate.getDate().setStart(isoDateTime);
		}
	}

	private String formatISO() {
		return "demo";
	}

}
