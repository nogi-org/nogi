package kr.co.nogibackend.config.exception;

import java.util.List;
import kr.co.nogibackend.response.code.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class GlobalException extends RuntimeException {

  private final Response res;
  private List errors;

  public GlobalException(Response res) {
    super(res.getMsg());
    this.res = res;
  }

  public GlobalException(Response res, List errors) {
    super(res.getMsg());
    this.res = res;
    this.errors = errors;
  }

}
