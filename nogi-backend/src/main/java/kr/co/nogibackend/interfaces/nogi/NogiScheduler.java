package kr.co.nogibackend.interfaces.nogi;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.nogibackend.application.nogi.NogiFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
  Package Name : kr.co.nogibackend.interfaces.notion
  File Name    : NotionScheduler
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NogiScheduler {

	private final NogiFacade nogiFacade;

	@Scheduled(cron = "0 */10 * * * *")
	public void onAuto() {
		log.info("onAuto Scheduler Start 10Min");
		nogiFacade.onAuto();
	}

}
