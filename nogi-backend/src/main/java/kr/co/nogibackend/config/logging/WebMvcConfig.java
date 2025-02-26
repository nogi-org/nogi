package kr.co.nogibackend.config.logging;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final HttpLoggingInterceptor httpLoggingInterceptor;
  private final SQLLoggingInterceptor sqlLoggingInterceptor;

  @Value("${decorator.datasource.p6spy.enable-logging:true}")
  private boolean enableSQLLogging;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(httpLoggingInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/vendor/**", "/css/*", "/img/*");

    if (enableSQLLogging) {
      registry.addInterceptor(sqlLoggingInterceptor)
          .addPathPatterns("/**");
    }
  }
}