package kr.co.nogibackend.interfaces.notice.response;

import kr.co.nogibackend.domain.notice.entity.Notice;

public record NoticeGetResponse(
    Long id,
    String title,
    String content,
    String url
) {

  public static NoticeGetResponse of(Notice notice) {
    return
        new NoticeGetResponse(
            notice.getId(),
            notice.getTitle(),
            notice.getContent(),
            notice.getUrl()
        );
  }

}
