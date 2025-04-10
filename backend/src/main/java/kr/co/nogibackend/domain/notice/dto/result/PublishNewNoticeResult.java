package kr.co.nogibackend.domain.notice.dto.result;

import kr.co.nogibackend.domain.user.User;

public record PublishNewNoticeResult(
    User user,
    boolean isSuccess
) {

}
