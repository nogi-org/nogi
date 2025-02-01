package kr.co.nogibackend.domain.notion;

import lombok.Getter;

@Getter
public enum NotionPropertyValue {

  STATUS_PENDING("대기"),
  STATUS_FAIL("실패"),
  STATUS_DONE("완료");

  private final String name;

  NotionPropertyValue(String name) {
    this.name = name;
  }

}
