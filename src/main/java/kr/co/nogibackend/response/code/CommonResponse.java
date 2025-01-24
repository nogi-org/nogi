package kr.co.nogibackend.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonResponse implements Response {

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
  F_UNKNOWN_EXTERNAL(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL-3", "External API UnKnown"),

  // 권한
  F_NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "AUTH-0", "유저를 찾을 수 없습니다."),
  F_ERROR_SIGNUP(HttpStatus.BAD_REQUEST, "AUTH-1", "회원가입 중 문제가 발생했습니다."),
  F_401(HttpStatus.BAD_REQUEST, "AUTH-2", "인증에 실패했습니다."),
  F_403(HttpStatus.BAD_REQUEST, "AUTH-3", "접근권한이 없습니다."),

  // 유저
  F_DUPLICATION_NICKNAME(HttpStatus.BAD_REQUEST, "USER-0", "닉네임 중복입니다."),

  // 태그
  F_DUPLICATION_TAG(HttpStatus.BAD_REQUEST, "TAG-0", "중복된 태그가 있습니다."),
  F_BAD_REQUEST_TAG(HttpStatus.BAD_REQUEST, "TAG-1", "요청한 태그의 정보가 없거나 정확하지 않습니다."),

  // 게시글
  F_NOT_FOUND_POST(HttpStatus.BAD_REQUEST, "POST-0", "게시글을 찾을 수 없습니다."),
  F_NOT_OWNER_OR_NOT_ROLE_POST(HttpStatus.BAD_REQUEST, "POST-1", "게시글의 작성자가 아니거나 권한이 없습니다."),

  // 게시글 카테고리
  F_NOT_FOUND_POST_CATEGORY(HttpStatus.BAD_REQUEST, "POST-CATEGORY-0", "게시글의 카테고리를 찾을 수 없습니다."),

  // 댓글
  F_NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "COMMENT-0", "댓글을 찾을 수 없습니다."),

  // 프로필
  F_NOT_FOUND_PROFILE_USER(HttpStatus.BAD_REQUEST, "PROFILE-0", "프로필 수정가능한 유저를 찾을 수 없습니다."),

  // 파일
  F_NOT_SUPPORT_EXTENSION(HttpStatus.BAD_REQUEST, "FILE-0", "지원하지 않는 확장자입니다."),
  F_FILE_DATA_REGISTER(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-1", "파일정보 저장 시 에러발생"),
  F_NOT_FOUND_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-2", "파일을 찾을 수 없습니다.");


  public final HttpStatus status;
  public final String code;
  public final String msg;

}
