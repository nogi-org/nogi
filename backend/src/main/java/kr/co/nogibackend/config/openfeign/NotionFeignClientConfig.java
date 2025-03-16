package kr.co.nogibackend.config.openfeign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import kr.co.nogibackend.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotionFeignClientConfig implements RequestInterceptor {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void apply(RequestTemplate template) {
    if (template.url().contains("oauth/token")) {
      return;
    }

    template.header("Notion-Version", "2022-06-28");
    template.header("Content-Type", "application/json");
    var newHeaders = HttpRequestUtils.handleBearerToken(
        template.headers());// 1ad7e38f-6917-811c-929c-fdab3e1c8b85

    // 기존 헤더 초기화 후 새 헤더 설정(초기화 되지 않는 문제가 있어서 초기화 후 설정)
    template.headers(null);
    template.headers(newHeaders);
  }

  // todo: 에러 핸들링 필요시 사용하기
  // @Override
  // public Exception decode(String methodKey, Response response) {
  // 	return response.;
  // }

}
