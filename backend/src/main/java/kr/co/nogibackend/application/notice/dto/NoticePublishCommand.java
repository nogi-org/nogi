package kr.co.nogibackend.application.notice.dto;

import kr.co.nogibackend.domain.notice.entity.Notice;

public record NoticePublishCommand(
    String title,
    String url,
    String content
) {

  public Notice buildInitCreateNotice() {
    return
        Notice
            .builder()
            .title(this.title)
            .url(this.url)
            .content(this.content)
            .build();
  }

}
