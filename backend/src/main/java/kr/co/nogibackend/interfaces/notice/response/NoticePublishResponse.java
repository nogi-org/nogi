package kr.co.nogibackend.interfaces.notice.response;

import java.util.List;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;

public record NoticePublishResponse(
    int userSize,
    List<NoticePublishResultResponse> result
) {

  public static NoticePublishResponse ofs(int userSize, List<NoticeUser> noticeUsers) {
    List<NoticePublishResultResponse> result =
        noticeUsers
            .stream()
            .map(noticeUser ->
                new NoticePublishResultResponse(
                    noticeUser.getUser().getId()
                    , noticeUser.isSuccess()
                    , noticeUser.getNotice().getId()
                )
            )
            .toList();
    return new NoticePublishResponse(userSize, result);
  }

  public record NoticePublishResultResponse(
      Long userId
      , boolean isSuccess
      , Long noticeId
  ) {

  }

}
