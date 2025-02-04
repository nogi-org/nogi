package kr.co.nogibackend.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotionResponseCode implements ResponseCode {

	F_FILE_URL_PARSING(HttpStatus.INTERNAL_SERVER_ERROR, "NOTION-0", "노션 File URL Parsing 실패");

	public final HttpStatus status;
	public final String code;
	public final String msg;

}
