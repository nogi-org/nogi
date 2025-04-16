package kr.co.nogibackend.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  public static String getTodayDateAsYYYYMMDDString() {
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return today.format(formatter);
  }

}
