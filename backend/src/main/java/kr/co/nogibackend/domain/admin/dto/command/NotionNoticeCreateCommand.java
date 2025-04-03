package kr.co.nogibackend.domain.admin.dto.command;

import kr.co.nogibackend.domain.notice.entity.Notice;

public record NotionNoticeCreateCommand(
		String title,
		String url,
		String content
) {

	public Notice buildNotice() {
		return
				Notice
						.builder()
						.url(this.url)
						.title(this.title)
						.content(this.content)
						.build();
	}

}
