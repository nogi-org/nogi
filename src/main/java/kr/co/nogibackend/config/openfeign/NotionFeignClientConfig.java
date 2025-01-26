package kr.co.nogibackend.config.openfeign;

import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/*
  Package Name : kr.co.nogibackend.config.openfeign
  File Name    : NotionFeignClientConfig
  Author       : superpil
  Created Date : 2023/08/19
  Description  : Notion 기본헤더 설정
 */
@Slf4j
@Configuration
public class NotionFeignClientConfig implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		template.header("Notion-Version", "2022-06-28");
		template.header("Content-Type", "application/json");
	}

}
