package kr.co.nogibackend.application.notice;

import java.util.List;
import kr.co.nogibackend.application.notice.dto.NoticePublishCommand;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.service.NoticeCreateService;
import kr.co.nogibackend.domain.notice.service.NoticePublishService;
import kr.co.nogibackend.domain.notion.dto.result.PublishNewNoticeResult;
import kr.co.nogibackend.interfaces.notice.response.NoticePublishResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticePublishFacade {

  private final NoticePublishService noticePublishService;
  private final NoticeCreateService noticeCreateService;

  @Transactional
  public NoticePublishResponse publish(NoticePublishCommand command) {
    Notice notice = noticeCreateService.create(command);
    List<PublishNewNoticeResult> results = noticePublishService.publishToNotion(notice);
    List<NoticeUser> histories = noticeCreateService.saveHistories(results, notice);
    return NoticePublishResponse.ofs(histories);
  }

}