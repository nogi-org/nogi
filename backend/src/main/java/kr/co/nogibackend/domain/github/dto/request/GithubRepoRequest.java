package kr.co.nogibackend.domain.github.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubRepoRequest(
    String name, // 저장소 이름
    @JsonProperty("auto_init")
    Boolean autoInit // 저장소 초기화 여부
) {

}
