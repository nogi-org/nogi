package kr.co.nogibackend.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

	public static final ZoneId koreaZone = ZoneId.of("Asia/Seoul");
	public static final ZoneId utcZone = ZoneId.of("UTC");

	public static String getTodayDateAsYYYYMMDDString() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return today.format(formatter);
	}

	public static String convertKoreaDateTimeFromUTC(LocalDateTime utcLocalDateTime) {
		ZonedDateTime utcZoned = utcLocalDateTime.atZone(ZoneId.of("UTC"));
		ZonedDateTime koreaZoned = utcZoned.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
		return koreaZoned.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

}
