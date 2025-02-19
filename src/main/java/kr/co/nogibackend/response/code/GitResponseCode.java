package kr.co.nogibackend.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GitResponseCode implements ResponseCode {

	F_SAMPLE_GIT(HttpStatus.BAD_REQUEST, "GIT-0", "샘플 깃 에러"),
	F_GIT_UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "GIT-1", "Github 에 요청하는 중 에러가 발생했어요"),
	F_DUPLICATION_REPO_NAME_GIT(HttpStatus.BAD_REQUEST, "GIT-2", "이미 존재하는 저장소 이름이에요"),
	F_PRIMARY_EMAIL_NOTFOUND(HttpStatus.NOT_FOUND, "GIT-3",
		"Primary Email을 찾을 수 없어요. Github에 Primary Email이 등록되어 있는지 확인해주세요"),
	;

	public final HttpStatus status;
	public final String code;
	public final String msg;

}
