package kr.co.nogibackend.config.openfeign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class NotionFeignClientConfig implements RequestInterceptor {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void apply(RequestTemplate template) {
    template.header("Notion-Version", "2022-06-28");
    template.header("Content-Type", "application/json");
  }

  // todo: 에러 핸들링 필요시 사용하기
  // @Override
  // public Exception decode(String methodKey, Response response) {
  // 	return response.;
  // }

}
