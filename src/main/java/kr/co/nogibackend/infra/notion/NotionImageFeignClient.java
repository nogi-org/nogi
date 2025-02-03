package kr.co.nogibackend.infra.notion;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.co.nogibackend.config.openfeign.NotionFeignClientConfig;

/*
  Package Name : kr.co.nogibackend.infra.notion
  File Name    : NotionImageFeignClient
  Author       : superpil
  Created Date : 25. 2. 3.
  Description  :
 */
@FeignClient(name = "NotionImageClient", url = "https://api.notion.com/v1", configuration = NotionFeignClientConfig.class)
public interface NotionImageFeignClient {

	@GetMapping(path = "{path}")
	byte[] getBlockImage(
		URI baseUrl
		, @PathVariable("path") String path
	);

}
