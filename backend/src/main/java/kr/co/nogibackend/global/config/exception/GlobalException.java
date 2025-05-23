package kr.co.nogibackend.global.config.exception;

import kr.co.nogibackend.global.response.code.ResponseCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

	private final ResponseCode code;
	private Object result;

	public GlobalException(ResponseCode code) {
		super(code.getMsg());
		this.code = code;
	}

	public GlobalException(ResponseCode code, Object result) {
		super(code.getMsg());
		this.code = code;
		this.result = result;
	}

}
