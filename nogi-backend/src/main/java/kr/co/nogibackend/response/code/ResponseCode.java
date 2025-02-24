package kr.co.nogibackend.response.code;

import org.springframework.http.HttpStatus;

public interface ResponseCode {

	HttpStatus getStatus();

	String getCode();

	String getMsg();

}
