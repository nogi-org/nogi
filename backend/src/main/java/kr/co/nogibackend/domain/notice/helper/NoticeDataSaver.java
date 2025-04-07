package kr.co.nogibackend.domain.notice.helper;

import java.util.List;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.repository.NoticeUserCreateRepository;
import kr.co.nogibackend.domain.notion.dto.result.PublishNewNoticeResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeDataSaver {

  private final NoticeUserCreateRepository noticeUserCreateRepository;

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
