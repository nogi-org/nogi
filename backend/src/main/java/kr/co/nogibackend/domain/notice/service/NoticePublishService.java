package kr.co.nogibackend.domain.notice.service;

import java.util.List;
import kr.co.nogibackend.domain.notice.dto.result.PublishNewNoticeResult;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.repository.NoticeUserGetRepository;
import kr.co.nogibackend.domain.notion.helper.NotionDataInjector;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticePublishService {

  private final UserRepository userRepository;
  private final NotionDataInjector notionDataInjector;
  private final NoticeUserGetRepository noticeUserGetRepository;

  @Transactional
  public List<PublishNewNoticeResult> publishToNotion(Notice notice) {
    List<User> users = userRepository.findAllUser();
    return notionDataInjector.publishNewNotice(users, notice);
  }

  // todo: isSuccess가 false인 유저만 가져와야함
  // todo: 쿼리 메소드 새로 따기
  @Transactional
  public List<PublishNewNoticeResult> rePublish(Long noticeId) {
    List<NoticeUser> noticeUsers =
        noticeUserGetRepository.searchByConditions(noticeId, false);

    if (noticeUsers.isEmpty()) {
      return List.of();
    }

    Notice notice = noticeUsers.get(0).getNotice();
    List<User> users = noticeUsers.stream().map(NoticeUser::getUser).toList();

    List<PublishNewNoticeResult> publishResults =
        notionDataInjector.publishNewNotice(users, notice);

    this.updateRePublishResult();
    return publishResults;
  }

  private void updateRePublishResult() {
    publishResults.forEach(result -> {
      Long userId = result.user().getId();
      noticeUsers.forEach(noticeUser -> {
        if (noticeUser.getUser().getId().equals(userId)) {
          noticeUser.updateIsSuccess(result.isSuccess());
        }
      });
    });
  }

}
