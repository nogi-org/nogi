package kr.co.nogibackend.domain.notice.service;

import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.repository.NoticeGetRepository;
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

  public PageImpl<Notice> getPage(Pageable pageable) {
    return noticeGetRepository.page(pageable);
  }

}
