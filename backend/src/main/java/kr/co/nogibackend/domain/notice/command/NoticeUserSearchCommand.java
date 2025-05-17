package kr.co.nogibackend.domain.notice.command;

public record NoticeUserSearchCommand(
		Long noticeId,
		Boolean isSuccess
) {

}
