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
  Description  : GitHub 기본헤더 설정
 */
@Slf4j
@Configuration
public class GitHubFeignClientConfig implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		template.header("Accept", "application/vnd.github+json");
	}

}
