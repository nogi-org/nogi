package kr.co.nogibackend.infra.notice.adapter;

import java.util.Optional;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.port.NoticeReadRepositoryPort;
import kr.co.nogibackend.infra.notice.jpa.NoticeReadJpa;
import kr.co.nogibackend.infra.notice.query.NoticeReadQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeReadRepositoryPortAdapter implements NoticeReadRepositoryPort {

	private final NoticeReadQuery noticeReadQuery;
	private final NoticeReadJpa noticeReadJpa;

	@Override
	public Page<Notice> getNotices(Pageable pageable) {
		return noticeReadQuery.getNotices(pageable);
	}

	@Override
	public Optional<Notice> findById(Long noticeId) {
		return noticeReadJpa.findById(noticeId);
	}

}
