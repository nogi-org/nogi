package kr.co.nogibackend.infra.notice.impl;

import java.util.List;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.repository.NoticeUserGetRepository;
import kr.co.nogibackend.infra.notice.query.NoticeUserGetQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeUserGetRepositoryImpl implements NoticeUserGetRepository {

  private final NoticeUserGetQueryRepository noticeUserGetQueryRepository;

  @Override
  public List<NoticeUser> findByNoticeIdWithUserAndNotice(Long noticeId) {
    return
        noticeUserGetQueryRepository.findByNoticeIdWithUserAndNotice(noticeId);
  }
}
