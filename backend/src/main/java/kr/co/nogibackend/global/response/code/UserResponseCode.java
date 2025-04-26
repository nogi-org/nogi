package kr.co.nogibackend.global.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserResponseCode implements ResponseCode {

	F_NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "USER-0", "유저를 찾을 수 없습니다."),
	F_FAIL_SIGNUP(HttpStatus.BAD_REQUEST, "USER-1", "회원가입 중 문제가 발생했습니다."),
	F_401(HttpStatus.UNAUTHORIZED, "USER-2", "인증에 실패했습니다."),
	F_403(HttpStatus.FORBIDDEN, "USER-3", "접근권한이 없습니다."),
	F_DUPLICATION_NICKNAME(HttpStatus.BAD_REQUEST, "USER-4", "닉네임 중복입니다."),
	F_MANUAL(HttpStatus.BAD_REQUEST, "USER-5", "수동실행 중 문제가 발생했어요."),
	F_NOT_FOUND_NOGI_BOT(HttpStatus.BAD_REQUEST, "USER-6", "NogiBot을 찾을 수 없어요."),
	F_REQUIRE_NOTION_INFO(HttpStatus.BAD_REQUEST, "USER-7", "유저에 필수 Notion정보가 없어요.");

	public final HttpStatus status;
	public final String code;
	public final String msg;

}
