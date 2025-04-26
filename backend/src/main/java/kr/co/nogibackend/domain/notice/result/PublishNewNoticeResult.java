package kr.co.nogibackend.domain.notice.result;

import kr.co.nogibackend.domain.user.entity.User;

public record PublishNewNoticeResult(
		User user,
		boolean isSuccess
) {

}
