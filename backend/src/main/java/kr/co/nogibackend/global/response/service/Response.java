package kr.co.nogibackend.global.response.service;

import static kr.co.nogibackend.global.response.code.CommonResponseCode.S_OK;

import kr.co.nogibackend.global.response.code.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Response {

	// 응답 데이터가 없는 성공
	public static <T> ResponseEntity<?> success() {
		return ResponseEntity.ok(new CommonResultSet<>(true, S_OK));
	}

	// 응답 데이터가 있는 성공
	public static <T> ResponseEntity<?> success(T result) {
		CommonResultSet<T> response = new CommonResultSet<>(true, S_OK);
		response.setResult(result);
		return ResponseEntity.ok(response);
	}

	// 응답 데이터가 없는 실패
	public static <T> ResponseEntity<?> fail(ResponseCode code) {
		CommonResultSet<T> response = new CommonResultSet<>(false, code);
		return new ResponseEntity<>(response, code.getStatus());
	}

	// 응답 데이터가 있는 실패
	public static <T> ResponseEntity<?> fail(ResponseCode code, T result) {
		CommonResultSet<T> response = new CommonResultSet<>(false, code);
		response.setResult(result);
		return new ResponseEntity<>(response, code.getStatus());
	}

}
