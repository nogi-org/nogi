package kr.co.nogibackend.infra.notice.impl;

import java.util.Optional;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.repository.NoticeGetRepository;
import kr.co.nogibackend.infra.notice.query.NoticeGetQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeGetRepositoryImpl implements NoticeGetRepository {

  private final NoticeGetQueryRepository noticeGetQueryRepository;
  private final NoticeGetRepository noticeGetRepository;

  @Override
  public PageImpl<Notice> page(Pageable pageable) {
    return noticeGetQueryRepository.page(pageable);
  }

  @Override
  public Optional<Notice> findById(Long noticeId) {
    return noticeGetRepository.findById(noticeId);
  }

}
