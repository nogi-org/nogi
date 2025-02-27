package kr.co.nogibackend.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT-Cookie");

    Components components = new Components()
        .addSecuritySchemes(
            "JWT-Cookie",
            new SecurityScheme()
                .name("ac_token") // 쿠키 이름과 동일하게 설정
                .type(SecurityScheme.Type.APIKEY) // API Key 방식 사용
                .in(In.COOKIE) // 쿠키에서 읽도록 설정
        );

    return new OpenAPI()
        .addSecurityItem(securityRequirement)
        .components(components);
  }
}
