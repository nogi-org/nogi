package kr.co.nogibackend.domain.notice.service;

import java.util.List;
import kr.co.nogibackend.domain.admin.dto.command.NoticePublishCommand;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.helper.NoticeDataSaver;
import kr.co.nogibackend.domain.notice.repository.NoticeCreateRepository;
import kr.co.nogibackend.domain.notion.dto.result.PublishNewNoticeResult;
import kr.co.nogibackend.domain.notion.helper.NotionDataInjector;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.UserRepository;
import kr.co.nogibackend.interfaces.notice.response.NoticePublishResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticePublishService {

  private final UserRepository userRepository;
  private final NoticeCreateRepository noticeCreateRepository;
  private final NotionDataInjector notionDataInjector;
  private final NoticeDataSaver noticeDataSaver;

  @Transactional
  public NoticePublishResponse publish(NoticePublishCommand command) {
    Notice notice = noticeCreateRepository.create(command.buildNotice());
    List<User> users = userRepository.findAllUser();
    List<PublishNewNoticeResult> results = notionDataInjector.publishNewNotice(users, notice);
    List<NoticeUser> histories = noticeDataSaver.saveHistories(results, notice);
    return NoticePublishResponse.ofs(users.size(), histories);
  }


  /*
  [request]
  1. 공지사항 id
  2. List{
    * noticeUserId
    * 유저id
  }
   */
  public void rePublish() {
    // 공지사항id로 공지사항 불러오기
    // noticeUserId로 noticeUser 테이블에 조회(패치조인으로) => List<NoticeUser>
    // 유저만 가져오기 noticeUser.stream().map(user -> user.getUser())
    // List<> result = publishNewNotice(users, notice) 호출
    // NoticeUser리스트를 순회하면서 Result 유저와 비교해서 성공한 유저는 noticeUser 테이블에 성공으로 업데이트
  }

}
