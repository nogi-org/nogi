package kr.co.nogibackend.interfaces.notion.controller;

import kr.co.nogibackend.application.notion.NotionLoginFacade;
import kr.co.nogibackend.application.notion.command.NotionLoginEventCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotionEventListenerV1 {

	private final NotionLoginFacade notionLoginFacade;

	@EventListener
	public void onNotionLoginEvent(NotionLoginEventCommand event) {
		notionLoginFacade.onNotionLogin(event);
	}

}
