package kr.co.nogibackend.domain.github.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubAddCollaboratorRequest(
    @JsonProperty("permission")
    String permission // 권한
) {

}
