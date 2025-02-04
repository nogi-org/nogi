package kr.co.nogibackend.infra.notion;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/*
base url 을 동적으로 처리
url = "dynamic-baseUrl" 가 없으면 bean 초기화가 안돼서 넣어뒀음. 아무런 의미 없음
URI baseUrl 에서 동적으로 base url 을 가져와서 처리함.
 */
@FeignClient(name = "NotionImageClient", url = "dynamic-baseUrl")
public interface NotionImageFeignClient {

	@GetMapping(path = "")
	byte[] getBlockImage(URI baseUrl);

}
