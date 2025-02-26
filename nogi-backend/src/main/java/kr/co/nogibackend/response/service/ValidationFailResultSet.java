package kr.co.nogibackend.response.service;

import lombok.Getter;

@Getter
public class ValidationFailResultSet {

  private final String code;
  private final String msg;
  private final String field;

  public ValidationFailResultSet(String code, String msg, String field) {
    this.code = code;
    this.msg = msg;
    this.field = field;
  }

}
