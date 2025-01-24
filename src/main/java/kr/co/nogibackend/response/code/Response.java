package kr.co.nogibackend.response.code;

import org.springframework.http.HttpStatus;

public interface Response {

  HttpStatus getStatus();

  String getCode();

  String getMsg();

}
