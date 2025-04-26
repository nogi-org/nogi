package kr.co.nogibackend.interfaces.notice.request;

import kr.co.nogibackend.application.notice.command.NoticePublishCommand;

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
