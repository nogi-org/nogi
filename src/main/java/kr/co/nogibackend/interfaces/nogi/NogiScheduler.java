package kr.co.nogibackend.interfaces.nogi;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.nogibackend.application.nogi.NogiFacade;
import lombok.RequiredArgsConstructor;

/*
  Package Name : kr.co.nogibackend.interfaces.notion
  File Name    : NotionScheduler
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Component
@RequiredArgsConstructor
public class NogiScheduler {

	private final NogiFacade nogiFacade;

	// 10분 = 600,000ms
	@Scheduled(cron = "0 */10 * * * *")
	public void onAuto() {
		// nogiFacade.onAuto();
	}

}
