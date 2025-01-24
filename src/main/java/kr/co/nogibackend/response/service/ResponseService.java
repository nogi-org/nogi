package kr.co.nogibackend.response.service;

import static kr.co.nogibackend.response.code.CommonResponse.S_OK;

import java.util.List;
import kr.co.nogibackend.response.code.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

  public static <T> ResponseEntity<?> success(T data) {
    DemoCommonResult<T> result = new DemoCommonResult<>();
    result.setSuccess(true);
    result.setCode(S_OK.getCode());
    result.setMsg(S_OK.getMsg());

    if (data instanceof List<?>) {
      result.setResults((List<T>) data);
    } else {
      result.setResult(data);
    }
    
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  public <T> ResponseEntity<SingleResult<T>> getSingleResult(T data) {
    SingleResult<T> result = new SingleResult<>();
    result.setData(data);
    setSuccessResult(result);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  public <T> ResponseEntity<ListResult<T>> getListResult(List<T> list) {
    ListResult<T> result = new ListResult<>();
    result.setList(list);
    setSuccessResult(result);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  public ResponseEntity<CommonResult> getSuccessResult() {
    CommonResult result = new CommonResult();
    setSuccessResult(result);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  private void setSuccessResult(CommonResult result) {
    result.setSuccess(true);
    result.setCode(S_OK.getCode());
    result.setMsg(S_OK.getMsg());
  }

  public ResponseEntity<CommonResult> getFailResult(Response err) {
    CommonResult result = new CommonResult();
    result.setSuccess(false);
    result.setCode(err.getCode());
    result.setMsg(err.getMsg());
    return new ResponseEntity<>(result, err.getStatus());
  }

  public <T> ResponseEntity<ListResult<T>> getFailResults(Response err, List errors) {
    ListResult<T> result = new ListResult<>();
    result.setSuccess(false);
    result.setCode(err.getCode());
    result.setMsg(err.getMsg());
    result.setList(errors);
    return new ResponseEntity<>(result, err.getStatus());
  }

}
