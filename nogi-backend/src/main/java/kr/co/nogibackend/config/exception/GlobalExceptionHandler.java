package kr.co.nogibackend.config.exception;

import static kr.co.nogibackend.response.code.CommonResponseCode.F_VALIDATION;

import java.util.List;
import kr.co.nogibackend.response.service.Response;
import kr.co.nogibackend.response.service.ValidationFailResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected <T> ResponseEntity<?> handleClientRequestValidException(
      BindException exception
  ) {
    List<ValidationFailResultSet> errors =
        exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new ValidationFailResultSet(error.getCode(), error.getDefaultMessage(),
                error.getField()))
            .toList();

    return Response.fail(F_VALIDATION, errors);
  }

  @ExceptionHandler(GlobalException.class)
  protected <T> ResponseEntity<?> handleGlobalException(GlobalException error) {
    return Response.fail(error.getCode(), error.getResult());
  }

}
