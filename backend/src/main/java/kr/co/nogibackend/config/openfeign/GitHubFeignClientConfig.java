package kr.co.nogibackend.config.openfeign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/*
  Package Name : kr.co.nogibackend.config.openfeign
  File Name    : GitHubFeignClientConfig
  Author       : superpil
  Created Date : 2023/08/19
  Description  : GitHub API 기본 헤더 설정 (Authorization, Accept)
 */
@Slf4j
@Configuration
public class GitHubFeignClientConfig implements RequestInterceptor {

  private static final String TOKEN_PREFIX = "Bearer ";
  private static final String AUTHORIZATION_HEADER = "Authorization";

  @Override
  public void apply(RequestTemplate template) {
    template.header("Accept", "application/vnd.github+json");

    var originalHeaders = template.headers();

    // Authorization 헤더를 제외한 새로운 헤더 맵 생성 (ImmutableMap 사용)
    var newHeaders = originalHeaders.entrySet().stream()
        .filter(entry -> !AUTHORIZATION_HEADER.equalsIgnoreCase(entry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    // Authorization 헤더가 존재하면 Bearer 추가
    originalHeaders.getOrDefault(AUTHORIZATION_HEADER, List.of())
        .stream().findFirst()
        .map(token -> token.startsWith(TOKEN_PREFIX) ? token : TOKEN_PREFIX + token)
        .ifPresent(token -> newHeaders.put(AUTHORIZATION_HEADER, List.of(token)));

    // 기존 헤더 초기화 후 새 헤더 설정
    template.headers(null);
    template.headers(newHeaders);
  }
}