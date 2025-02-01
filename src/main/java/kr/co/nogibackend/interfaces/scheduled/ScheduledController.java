package kr.co.nogibackend.interfaces.scheduled;

import kr.co.nogibackend.application.scheduled.ScheduledFacade;
import lombok.RequiredArgsConstructor;

/*
  Package Name : kr.co.nogibackend.interfaces.scheduled
  File Name    : ScheduledController
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@RequiredArgsConstructor
public class ScheduledController {

	private final ScheduledFacade scheduledFacade;

	// todo: 여기서 nogi 스케쥴 돌림
	public void on() {
		scheduledFacade.onNogi();
	}

}
