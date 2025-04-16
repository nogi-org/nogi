package kr.co.nogibackend.config.openfeign;

import feign.Logger;
import feign.Request;
import feign.Response;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomFeignLogger extends Logger {

  @Override
  protected void log(String configKey, String format, Object... args) {
    log.info(String.format(methodTag(configKey) + format, args));
  }

  @Override
  protected void logRequest(String configKey, Level logLevel, Request request) {
    log.info("\n===========================[Feign Request]===========================");
    super.logRequest(configKey, logLevel, request);
  }

  @Override
  protected Response logAndRebufferResponse(String configKey, Level logLevel,
      Response response, long elapsedTime) throws IOException {
    log.info("\n===========================[Feign Response]==========================");
    return super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
  }
}

