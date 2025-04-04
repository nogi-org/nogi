package kr.co.nogibackend.interfaces.notice.request;

import kr.co.nogibackend.domain.admin.dto.command.NoticePublishCommand;

public record NoticePublishRequest(
    String title,
    String url,
    String content
) {

  public NoticePublishCommand toCommand() {
    return
        new NoticePublishCommand(this.title, this.url, this.content);
  }

}
