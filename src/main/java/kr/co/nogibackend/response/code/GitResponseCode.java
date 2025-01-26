package kr.co.nogibackend.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GitResponseCode implements ResponseCode {

	F_SAMPLE_GIT(HttpStatus.BAD_REQUEST, "GIT-0", "샘플 깃 에러");

	public final HttpStatus status;
	public final String code;
	public final String msg;

}
