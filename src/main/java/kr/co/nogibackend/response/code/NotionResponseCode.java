package kr.co.nogibackend.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotionResponseCode implements ResponseCode {

	F_SAMPLE_NOTION(HttpStatus.BAD_REQUEST, "NOTION-0", "샘플 노션 에러");

	public final HttpStatus status;
	public final String code;
	public final String msg;

}
