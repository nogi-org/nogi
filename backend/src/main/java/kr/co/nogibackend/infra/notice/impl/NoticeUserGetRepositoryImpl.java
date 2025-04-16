package kr.co.nogibackend.infra.notice.impl;

import java.util.List;
import kr.co.nogibackend.domain.notice.dto.condition.NoticeUserSearchConditions;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.repository.NoticeUserGetRepository;
import kr.co.nogibackend.infra.notice.query.NoticeUserGetQueryRepository;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeUserGetRepositoryImpl implements NoticeUserGetRepository {

  private final NoticeUserGetQueryRepository noticeUserGetQueryRepository;

  @Override
  public Page<NoticeUser> findRecipientsPage(
      Long noticeId
      , NoticeRecipientsRequest request
      , Pageable pageable
  ) {
    return noticeUserGetQueryRepository.findRecipientsPage(noticeId, request, pageable);
  }

  @Override
  public List<NoticeUser> searchByConditions(NoticeUserSearchConditions conditions) {
    return noticeUserGetQueryRepository.searchByConditions(conditions);
  }

}
