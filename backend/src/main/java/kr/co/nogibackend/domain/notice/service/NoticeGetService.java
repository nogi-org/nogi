package kr.co.nogibackend.domain.notice.service;

import static kr.co.nogibackend.response.code.NoticeResponseCode.F_NOT_FOUND_NOTICE;

import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.repository.NoticeGetRepository;
import kr.co.nogibackend.domain.notice.repository.NoticeUserGetRepository;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import kr.co.nogibackend.interfaces.notice.response.NoticeGetResponse;
import kr.co.nogibackend.interfaces.notice.response.NoticeRecipientsResponse;
import kr.co.nogibackend.interfaces.notice.response.NoticesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeGetService {

  private final NoticeGetRepository noticeGetRepository;
  private final NoticeUserGetRepository noticeUserGetRepository;

  public Page<NoticesResponse> getNotices(Pageable pageable) {
    return NoticesResponse.of(noticeGetRepository.getNotices(pageable));
  }

  public NoticeGetResponse getNotice(Long noticeId) {
    Notice notice =
        noticeGetRepository
            .findById(noticeId)
            .orElseThrow(() -> new GlobalException(F_NOT_FOUND_NOTICE));
    return NoticeGetResponse.of(notice);
  }

  @Transactional(readOnly = true)
  public Page<NoticeRecipientsResponse> getRecipients(
      Long noticeId
      , NoticeRecipientsRequest request
      , Pageable pageable
  ) {
    Page<NoticeUser> noticeUsers =
        noticeUserGetRepository.findRecipientsPage(noticeId, request, pageable);
    return NoticeRecipientsResponse.of(noticeUsers);
  }

}
