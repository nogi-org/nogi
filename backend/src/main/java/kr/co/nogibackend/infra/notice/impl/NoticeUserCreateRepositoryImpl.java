package kr.co.nogibackend.infra.notice.impl;

import java.util.List;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.repository.NoticeUserCreateRepository;
import kr.co.nogibackend.infra.notice.jpa.NoticeUserCreateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeUserCreateRepositoryImpl implements NoticeUserCreateRepository {

  private final NoticeUserCreateJpaRepository noticeUserCreateJpaRepository;

  @Override
  public List<NoticeUser> saveAll(List<NoticeUser> noticeUser) {
    return noticeUserCreateJpaRepository.saveAll(noticeUser);
  }

}
