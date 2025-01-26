package kr.co.nogibackend.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserResponseCode implements ResponseCode {

	F_NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "USER-0", "유저를 찾을 수 없습니다."),
	F_FAIL_SIGNUP(HttpStatus.BAD_REQUEST, "USER-1", "회원가입 중 문제가 발생했습니다."),
	F_401(HttpStatus.BAD_REQUEST, "USER-2", "인증에 실패했습니다."),
	F_403(HttpStatus.BAD_REQUEST, "USER-3", "접근권한이 없습니다."),
	F_DUPLICATION_NICKNAME(HttpStatus.BAD_REQUEST, "USER-4", "닉네임 중복입니다.");

	public final HttpStatus status;
	public final String code;
	public final String msg;

}
