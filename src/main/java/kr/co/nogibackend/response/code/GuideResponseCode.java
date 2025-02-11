package kr.co.nogibackend.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GuideResponseCode implements ResponseCode {

	F_DUPLICATION_ORDER(HttpStatus.INTERNAL_SERVER_ERROR, "GUIDE-0", "중복된 가이드 순서");

	public final HttpStatus status;
	public final String code;
	public final String msg;

}
