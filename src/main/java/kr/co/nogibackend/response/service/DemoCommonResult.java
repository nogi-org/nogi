package kr.co.nogibackend.response.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class DemoCommonResult<T> {

  private boolean success;
  private String code;
  private String msg;

  private List<T> results;
  private T result;

  private String field;

}
