package kr.co.nogibackend.domain.notion.dto.property;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.nogibackend.domain.notion.dto.constant.NotionColor;
import lombok.Getter;
import lombok.Setter;

/*
reference:
https://developers.notion.com/reference/property-object#date
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotionNogiProperties {

  @JsonIgnore
  private final ZoneId koreaZone = ZoneId.of("Asia/Seoul");
  @JsonIgnore
  private final ZoneId utcZone = ZoneId.of("UTC");

  private NotionNogiCategoryProperty nogiCategory;
  private NotionNogiCommitDateProperty nogiCommitDate;
  private NotionNogiStatusProperty nogiStatus;
  private NotionNogiTitleProperty nogiTitle;
  private NotionNogiCommitMessageProperty nogiCommitMessage;

  public static NotionNogiProperties createNewNotice(String title) {
    NotionNogiTitleProperty titles =
        NotionNogiTitleProperty.of(List.of(title));
    NotionNogiStatusProperty status =
        NotionNogiStatusProperty.of("운영", NotionColor.RED);
    NotionNogiCategoryProperty category =
        NotionNogiCategoryProperty.of("공지", NotionColor.BLUE);
    NotionNogiCommitDateProperty commitDate =
        NotionNogiCommitDateProperty.buildTodayDateAsYYYYMMDDString();

    NotionNogiProperties notionNogiProperties = new NotionNogiProperties();
    notionNogiProperties.setNogiTitle(titles);
    notionNogiProperties.setNogiStatus(status);
    notionNogiProperties.setNogiCategory(category);
    notionNogiProperties.setNogiCommitDate(commitDate);

    return notionNogiProperties;
  }

  // 카테고리가 깃헙의 디렉토리 경로로 사용됨(ex: java/문법)
  @JsonIgnore
  public String getCategoryPath() {
    return
        nogiCategory
            .getMulti_select()
            .stream()
            .map(NotionMultiSelectProperty::getName)
            .collect(Collectors.joining("/"));
  }

  /*
  github 에 UTC_ISO 날짜 포맷으로 커밋할 수 있다.
  notion 에서 받은 날짜는 한국 시간이다.
  한국 시간을 UTC 시간으로 변환 후 커밋하면 깃허브에서 한국시간으로 등록된다.
  예) notion 에서 2024-02-22 23:00:00 -> 2024:02:22 14:00:00 UTC 기준으로 변환 후 커밋하면 2024-02-22 23:00:00로 등록됨
  (한국시간 -9시간 -> UTC 시간)
   */
  public void createCommitDateWithCurrentTime() {
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // 커밋 날짜가 없을 경우
    if (this.getNogiCommitDate().getDate() == null) {
      String utc_iso = this.convertToUTC_ISO(LocalDateTime.now(koreaZone));

      NotionDateProperty notionDateProperty = new NotionDateProperty();
      notionDateProperty.setStart(utc_iso);

      NotionNogiCommitDateProperty notionNogiCommitDateProperty = new NotionNogiCommitDateProperty();
      notionNogiCommitDateProperty.setDate(notionDateProperty);

      this.nogiCommitDate = notionNogiCommitDateProperty;
      return;
    }

    LocalDateTime commitDateTime;
    try {
      // 날짜와 시간 모두 있는 경우
      commitDateTime =
          LocalDateTime.parse(this.nogiCommitDate.getDate().getStart(), dateTimeFormatter);
    } catch (DateTimeParseException error) {
      // 날짜만 있고 시간이 없는 경우
      LocalDate parsedDate =
          LocalDate.parse(this.nogiCommitDate.getDate().getStart(), dateFormatter);
      LocalDateTime nowKoreaTime = LocalDateTime.now(koreaZone);
      commitDateTime = parsedDate.atTime(nowKoreaTime.toLocalTime());
    }

    String utc_iso = this.convertToUTC_ISO(commitDateTime);
    this.nogiCommitDate.getDate().setStart(utc_iso);
  }

  // 커밋 메시지 속성을 문자열로 변환
  public String convertCommitMessageToString() {
    if (
        this.getNogiCommitMessage() == null ||
            this.getNogiCommitMessage().getRich_text().isEmpty()
    ) {
      return null;
    }
    StringBuilder strb = new StringBuilder();
    this.getNogiCommitMessage().getRich_text().forEach(text -> {
      strb.append(text.getPlain_text()).append("\n");
    });
    return strb.toString();
  }

  private String convertToUTC_ISO(LocalDateTime koreaLocalDateTime) {
    ZonedDateTime kstDateTime = koreaLocalDateTime.atZone(koreaZone);
    ZonedDateTime utcDateTime = kstDateTime.withZoneSameInstant(utcZone);
    return utcDateTime.format(DateTimeFormatter.ISO_INSTANT);
  }

}
