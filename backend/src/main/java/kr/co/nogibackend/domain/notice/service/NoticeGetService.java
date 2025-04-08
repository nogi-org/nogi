package kr.co.nogibackend.domain.notice.service;

import static kr.co.nogibackend.response.code.NoticeResponseCode.F_NOT_FOUND_NOTICE;

import java.util.List;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.repository.NoticeGetRepository;
import kr.co.nogibackend.domain.notice.repository.NoticeUserGetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeGetService {

  private final NoticeGetRepository noticeGetRepository;
  private final NoticeUserGetRepository noticeUserGetRepostiory;

  public PageImpl<Notice> getPage(Pageable pageable) {
    return noticeGetRepository.page(pageable);
  }

  public Notice getDetail(Long noticeId) {
    return
        noticeGetRepository
            .findById(noticeId)
            .orElseThrow(() -> new GlobalException(F_NOT_FOUND_NOTICE));
  }

  public List<Notice> getRecipients(Long noticeId) {
    List<NoticeUser> noticeUsers =
        noticeUserGetRepostiory.findByNoticeIdWithUserAndNotice(noticeId);
    return null;
  }

}
