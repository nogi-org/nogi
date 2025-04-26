package kr.co.nogibackend.application.notion;

import kr.co.nogibackend.application.notion.command.NotionLoginEventCommand;
import kr.co.nogibackend.domain.notion.service.NotionAccountService;
import kr.co.nogibackend.domain.user.command.UserUpdateCommand;
import kr.co.nogibackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotionLoginFacade {

	private final NotionAccountService notionAccountService;
	private final UserService userService;

	@Async
	@Retryable(
			value = {Exception.class},
			maxAttempts = 5, // 최대 5회 시도
			backoff = @Backoff(delay = 2000) // 2초 간격으로 재시도
	)
	public void onNotionLogin(NotionLoginEventCommand event) {
		// 1. notion database id 조회
		String notionDatabaseId = notionAccountService.getNotionDatabaseInfo(
				event.notionAccessToken(),
				event.notionPageId()
		);

		// 2. user 정보 업데이트
		userService.updateUser(
				UserUpdateCommand.builder()
						.id(event.userId())
						.notionDatabaseId(notionDatabaseId)
						.build()
		);
	}
}
