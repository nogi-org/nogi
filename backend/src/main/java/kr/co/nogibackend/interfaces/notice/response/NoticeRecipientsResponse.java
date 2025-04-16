package kr.co.nogibackend.interfaces.notice.response;

import java.time.LocalDateTime;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import org.springframework.data.domain.Page;

public record NoticeRecipientsResponse(
    Long userId,
    String githubOwner,
    boolean isSuccess,
    LocalDateTime createdAt
) {

  public static Page<NoticeRecipientsResponse> of(Page<NoticeUser> noticeUsers) {
    return
        noticeUsers
            .map(noticeUser ->
                new NoticeRecipientsResponse(
                    noticeUser.getUser().getId()
                    , noticeUser.getUser().getGithubOwner()
                    , noticeUser.isSuccess()
                    , noticeUser.getCreatedAt()
                )
            );
  }

}
