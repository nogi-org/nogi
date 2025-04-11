package kr.co.nogibackend.domain.notice.service;

import java.util.List;
import kr.co.nogibackend.domain.notice.dto.condition.NoticeUserSearchConditions;
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

  @Transactional
  public List<PublishNewNoticeResult> rePublish(Long noticeId) {
    List<NoticeUser> noticeUsers =
        noticeUserGetRepository
            .searchByConditions(new NoticeUserSearchConditions(noticeId, false));

    if (noticeUsers.isEmpty()) {
      return List.of();
    }

    Notice notice = noticeUsers.get(0).getNotice();
    List<User> users = noticeUsers.stream().map(NoticeUser::getUser).toList();

    List<PublishNewNoticeResult> results =
        notionDataInjector.publishNewNotice(users, notice);

    noticeUsers.forEach(noticeUser -> noticeUser.updateIsSuccess(results));
    return results;
  }

}
