package kr.co.nogibackend.domain.notice.port;

import java.util.List;
import kr.co.nogibackend.domain.notice.command.NoticeUserSearchCommand;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeUserReadRepositoryPort {

	Page<NoticeUser> findRecipientsPage
			(Long noticeId, NoticeRecipientsRequest request, Pageable pageable);

	List<NoticeUser> searchByConditions(NoticeUserSearchCommand conditions);

}
