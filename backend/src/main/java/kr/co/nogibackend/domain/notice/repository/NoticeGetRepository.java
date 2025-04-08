package kr.co.nogibackend.domain.notice.repository;

import kr.co.nogibackend.domain.notice.entity.Notice;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface NoticeGetRepository {

  PageImpl<Notice> page(Pageable pageable);

}
