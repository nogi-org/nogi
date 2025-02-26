package kr.co.nogibackend.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GuideResponseCode implements ResponseCode {

  F_DUPLICATION_ORDER(HttpStatus.INTERNAL_SERVER_ERROR, "GUIDE-0", "중복된 가이드 순서"),
  F_NOT_FOUND_GUIDE(HttpStatus.INTERNAL_SERVER_ERROR, "GUIDE-1", "가이드를 찾을 수 없음");

  public final HttpStatus status;
  public final String code;
  public final String msg;

}
