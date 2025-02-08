package kr.co.nogibackend.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotionResponseCode implements ResponseCode {

	F_FILE_URL_PARSING(HttpStatus.INTERNAL_SERVER_ERROR, "NOTION-0", "노션 File URL Parsing 실패"),
	F_GET_BLOCK_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "NOTION-1", "노션 이미지 파일을 불러오기 실패"),
	F_UPDATE_TIL_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "NOTION-2", "노션 TIL 성공 여부 상태 업데이트 실패");

	public final HttpStatus status;
	public final String code;
	public final String msg;

}
