package kr.co.nogibackend.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonResponseCode implements ResponseCode {

  /*
  성공
   */
  S_OK(HttpStatus.OK, "SUCCESS-0", "정상처리 완료"),

  /*
  실패
   */
  F_VALIDATION(HttpStatus.BAD_REQUEST, "COMMON-0", "유효성 검사 실패"),
  F_UNKNOWN(HttpStatus.BAD_REQUEST, "COMMON-1", "알 수 없는 오류"),
  F_NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-3", "NULL POINT"),
  F_IO(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-4", "입출력에 문제가 발생했습니다."),

  // 외부 서비스
  F_401_EXTERNAL_API(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL-0", "External API 401 Error"),
  F_400_EXTERNAL_API(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL-1", "External API 400 Error"),
  F_5xx_EXTERNAL_API(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL-2", "External API 5xx Error"),
  F_UNKNOWN_EXTERNAL(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL-3", "External API UnKnown");

  public final HttpStatus status;
  public final String code;
  public final String msg;

}
