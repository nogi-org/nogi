package kr.co.nogibackend.config.openfeign;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignApacheHttpConfiguration {

  @Bean
  public Client client() {
    return new ApacheHttpClient();
  }

}
