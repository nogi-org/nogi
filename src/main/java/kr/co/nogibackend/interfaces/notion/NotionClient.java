package kr.co.nogibackend.interfaces.notion;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.nogibackend.config.openfeign.NotionFeignClientConfig;
import kr.co.nogibackend.interfaces.notion.response.DemoResponse;

/*
  Package Name : kr.co.nogibackend.interfaces.notion
  File Name    : NotionOutController
  Author       : superpil
  Created Date : 25. 1. 26.
  Description  :
 */
@FeignClient(name = "NotionClient", url = "https://jsonplaceholder.typicode.com", configuration = NotionFeignClientConfig.class)
public interface NotionClient {

	@GetMapping("/posts")
	ResponseEntity<List<DemoResponse>> getDemo();

}
