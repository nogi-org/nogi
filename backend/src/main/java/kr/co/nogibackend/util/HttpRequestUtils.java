package kr.co.nogibackend.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class HttpRequestUtils {

  private static final String TOKEN_PREFIX = "Bearer ";
  private static final String AUTHORIZATION_HEADER = "Authorization";

  /**
   * Bearer 토큰을 추가한 새로운 헤더 맵을 반환합니다.
   *
   * @param originalHeaders 기존 헤더 맵
   * @return Bearer 토큰이 추가된 새로운 헤더 맵
   */
  @NotNull
  public static Map<String, Collection<String>> handleBearerToken(
      Map<String, Collection<String>> originalHeaders
  ) {
    // Authorization 헤더를 제외한 새로운 헤더 맵 생성 (ImmutableMap 사용)
    Map<String, Collection<String>> newHeaders = originalHeaders.entrySet().stream()
        .filter(entry -> !AUTHORIZATION_HEADER.equalsIgnoreCase(entry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    // Authorization 헤더가 존재하면 Bearer 추가
    originalHeaders.getOrDefault(AUTHORIZATION_HEADER, List.of())
        .stream().findFirst()
        .map(token -> token.startsWith(TOKEN_PREFIX) ? token : TOKEN_PREFIX + token)
        .ifPresent(token -> newHeaders.put(AUTHORIZATION_HEADER, List.of(token)));
    return newHeaders;
  }

}
