package kr.co.nogibackend.infra.notice.adapter;

import java.util.List;
import kr.co.nogibackend.domain.notice.command.NoticeUserSearchCommand;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.port.NoticeUserReadRepositoryPort;
import kr.co.nogibackend.infra.notice.query.NoticeUserReadQuery;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeUserReadRepositoryPortAdapter implements NoticeUserReadRepositoryPort {

	private final NoticeUserReadQuery noticeUserReadQuery;

	@Override
	public Page<NoticeUser> findRecipientsPage(
			Long noticeId
			, NoticeRecipientsRequest request
			, Pageable pageable
	) {
		return noticeUserReadQuery.findRecipientsPage(noticeId, request, pageable);
	}

	@Override
	public List<NoticeUser> searchByConditions(NoticeUserSearchCommand conditions) {
		return noticeUserReadQuery.searchByConditions(conditions);
	}

}
