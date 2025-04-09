package kr.co.nogibackend.domain.notice.repository;

import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeUserGetRepository {

  Page<NoticeUser> findRecipientsPage
      (Long noticeId, NoticeRecipientsRequest request, Pageable pageable);

}
