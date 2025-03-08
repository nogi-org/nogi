package kr.co.nogibackend.interfaces.notion;

import kr.co.nogibackend.application.notion.NotionFacade;
import kr.co.nogibackend.application.notion.dto.NotionLoginEventCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotionEventListener {

  private final NotionFacade notionFacade;

  @EventListener
  public void onNotionLoginEvent(NotionLoginEventCommand event) {
    notionFacade.onNotionLogin(event);
  }

}
