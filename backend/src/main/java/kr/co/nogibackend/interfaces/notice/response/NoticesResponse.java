package kr.co.nogibackend.interfaces.notice.response;

import java.time.LocalDateTime;
import kr.co.nogibackend.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;

public record NoticesResponse(
    Long id,
    String title,
    LocalDateTime createdAt
) {

  public static Page<NoticesResponse> of(Page<Notice> notice) {
    return
        notice
            .map(notice1 ->
                new NoticesResponse(
                    notice1.getId()
                    , notice1.getTitle()
                    , notice1.getCreatedAt()
                )
            );
  }

}
