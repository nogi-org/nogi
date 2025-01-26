package kr.co.nogibackend.response.service;

import com.fasterxml.jackson.annotation.JsonInclude;

import kr.co.nogibackend.response.code.ResponseCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class CommonResultSet<T> {

	private final boolean success;
	private final String code;
	private final String msg;
	@Setter
	private T result;

	public CommonResultSet(boolean success, ResponseCode code) {
		this.success = success;
		this.code = code.getCode();
		this.msg = code.getMsg();
	}

}
