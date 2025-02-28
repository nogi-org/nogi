package kr.co.nogibackend.domain.notion.constant;

import lombok.Getter;

@Getter
public enum NotionPropertyKey {

  STATUS("nogiStatus"),
  CATEGORY("nogiCategory"),
  TITLE("nogiTitle"),
  CREATE_DATE("nogiCommitDate");

  private final String name;

  NotionPropertyKey(String name) {
    this.name = name;
  }

}
