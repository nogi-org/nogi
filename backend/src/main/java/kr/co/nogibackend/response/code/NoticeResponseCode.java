package kr.co.nogibackend.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NoticeResponseCode implements ResponseCode {

  F_NOT_FOUND_NOTICE
      (HttpStatus.INTERNAL_SERVER_ERROR, "NOTICE-0", "공지사항을 찾을 수 없습니다.");

  public final HttpStatus status;
  public final String code;
  public final String msg;

}
