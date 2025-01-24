package kr.co.nogibackend.config.exception;

import static kr.co.nogibackend.response.code.CommonResponse.F_VALIDATION;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.nogibackend.response.service.ErrorResult;
import kr.co.nogibackend.response.service.ListResult;
import kr.co.nogibackend.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final ResponseService responseService;

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<ListResult<Object>> handleClientRequestValidException(
      BindException exception
  ) {
    BindingResult bindResult = exception.getBindingResult();
    List<FieldError> bindResultList = bindResult.getFieldErrors();
    List<ErrorResult> errorDtos = bindResultList.stream().map(error -> {
      ErrorResult errorDto = new ErrorResult();
      errorDto.setCode(error.getCode());
      errorDto.setField(error.getField());
      errorDto.setMsg(error.getDefaultMessage());
      return errorDto;
    }).collect(Collectors.toList());

    return responseService.getFailResults(F_VALIDATION, errorDtos);
  }

  @ExceptionHandler(GlobalException.class)
  protected ResponseEntity<ListResult<Object>> handleGlobalException(GlobalException error) {
    return responseService.getFailResults(error.getRes(), error.getErrors());
  }

}
