package kr.co.nogibackend.domain.notion.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NotionGetAccessInfo(
    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("bot_id")
    String botId, // 이 승인에 대한 식별자

    @JsonProperty("duplicated_template_id")
    String duplicatedTemplateId, // 복제된 페이지의 페이지 ID

    Owner owner, // 내부 객체로 정의

    @JsonProperty("workspace_icon")
    String workspaceIcon,

    @JsonProperty("workspace_id")
    String workspaceId,

    @JsonProperty("workspace_name")
    String workspaceName
) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Owner(
      String type, // "workspace" 또는 "user"
      User user // type이 "user"일 경우 존재
  ) {

  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record User(
      @JsonProperty("id")
      String id
  ) {

  }
}