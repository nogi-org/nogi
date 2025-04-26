package kr.co.nogibackend.interfaces.sync.controller;

import kr.co.nogibackend.application.sync.NotionGithubSyncFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotionGithubSyncSchedulerV1 {

	private final NotionGithubSyncFacade notionGithubSyncFacade;

	@Scheduled(cron = "0 */10 * * * *")
	public void onAutoSync() {
		notionGithubSyncFacade.onAutoSync();
	}

}
