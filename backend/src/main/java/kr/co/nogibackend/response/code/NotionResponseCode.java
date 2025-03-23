package kr.co.nogibackend.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotionResponseCode implements ResponseCode {

	F_FILE_URL_PARSING
			(HttpStatus.INTERNAL_SERVER_ERROR, "NOTION-0", "Notion File URL Parsing 실패"),

	F_GET_BLOCK_IMAGE
			(HttpStatus.INTERNAL_SERVER_ERROR, "NOTION-1", "Notion 이미지 파일을 불러오기 실패"),

	F_UPDATE_TIL_STATUS
			(HttpStatus.INTERNAL_SERVER_ERROR, "NOTION-2", "Notion TIL 성공 여부 상태 업데이트 실패"),

	F_GET_NOTION_PAGE
			(HttpStatus.BAD_REQUEST, "NOTION-3", "Notion 페이지 불러오기 실패"),

	F_GET_NOTION_BLOCK
			(HttpStatus.BAD_REQUEST, "NOTION-4", "Notion 블럭 불러오기 실패"),

	F_GET_NOTION_DATABASE
			(HttpStatus.BAD_REQUEST, "NOTION-5", "Notion 데이터베이스 조회 실패"),

	F_PREPROCESS_MARKDOWN
			(HttpStatus.INTERNAL_SERVER_ERROR, "NOTION-6", "Notion Block을 마크다운 전처리 과정에서 실패"),

	F_PROCESS_MARKDOWN
			(HttpStatus.INTERNAL_SERVER_ERROR, "NOTION-7", "Notion Block을 마크다운 변환 과정에서 실패");

	public final HttpStatus status;
	public final String code;
	public final String msg;

}
