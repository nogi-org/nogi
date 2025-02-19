package kr.co.nogibackend.interfaces.notion;

import kr.co.nogibackend.application.nogi.NogiFacade;
import lombok.RequiredArgsConstructor;

/*
  Package Name : kr.co.nogibackend.interfaces.notion
  File Name    : NotionScheduler
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@RequiredArgsConstructor
public class NotionScheduler {

	private final NogiFacade nogiFacade;

	// todo: 여기서 nogi 스케쥴 돌림
	public void on() {
		nogiFacade.onAuto();
	}

}
