package kr.co.nogibackend.infra.notice.impl;

import java.util.Optional;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.repository.NoticeGetRepository;
import kr.co.nogibackend.infra.notice.jpa.NoticeGetJpaRepository;
import kr.co.nogibackend.infra.notice.query.NoticeGetQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeGetRepositoryImpl implements NoticeGetRepository {

  private final NoticeGetQueryRepository noticeGetQueryRepository;
  private final NoticeGetJpaRepository noticeGetJpaRepository;

  @Override
  public Page<Notice> getNotices(Pageable pageable) {
    return noticeGetQueryRepository.getNotices(pageable);
  }

  @Override
  public Optional<Notice> findById(Long noticeId) {
    return noticeGetJpaRepository.findById(noticeId);
  }

}
