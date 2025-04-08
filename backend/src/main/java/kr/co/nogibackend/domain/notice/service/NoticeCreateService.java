package kr.co.nogibackend.domain.notice.service;

import java.util.List;
import kr.co.nogibackend.application.notice.dto.NoticePublishCommand;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.repository.NoticeCreateRepository;
import kr.co.nogibackend.domain.notice.repository.NoticeUserCreateRepository;
import kr.co.nogibackend.domain.notion.dto.result.PublishNewNoticeResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeCreateService {

  private final NoticeUserCreateRepository noticeUserCreateRepository;
  private final NoticeCreateRepository noticeCreateRepository;

  @Transactional
  public Notice create(NoticePublishCommand command) {
    Notice notice = noticeCreateRepository.create(command.buildInitCreateNotice());
    notice.updateUrl();
    return notice;
  }

  @Transactional
  public List<NoticeUser> saveHistories(List<PublishNewNoticeResult> results, Notice notice) {
    try {
      List<NoticeUser> noticeUsers =
          results
              .stream()
              .map(result ->
                  NoticeUser
                      .builder()
                      .notice(notice)
                      .user(result.user())
                      .isSuccess(result.isSuccess())
                      .build()
              )
              .toList();
      noticeUserCreateRepository.saveAll(noticeUsers);
      return noticeUsers;
    } catch (Exception error) {
      log.error("[Notice Database Save Error] 정상 발행 유저ID: {}, 발행 실패 유저ID: {}",
          results
              .stream()
              .filter(PublishNewNoticeResult::isSuccess)
              .map(r -> r.user().getId()).toList(),
          results
              .stream()
              .filter(r -> !r.isSuccess())
              .map(r -> r.user().getId()).toList()
      );
      return List.of();
    }
  }

}
