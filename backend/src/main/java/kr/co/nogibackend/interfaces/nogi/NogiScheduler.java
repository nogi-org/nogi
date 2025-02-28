package kr.co.nogibackend.interfaces.nogi;

import kr.co.nogibackend.application.nogi.NogiFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
