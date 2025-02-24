package kr.co.nogibackend.config.openfeign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;
import feign.httpclient.ApacheHttpClient;

/*
  Package Name : kr.co.nogibackend.config.openfeign
  File Name    : FeignApacheHttpConfiguration
  Author       : superpil
  Created Date : 25. 2. 4.
  Description  : FeignClient PATCH method 사용을 위한 설정
 */
@Configuration
public class FeignApacheHttpConfiguration {

	@Bean
	public Client client() {
		return new ApacheHttpClient();
	}

}
