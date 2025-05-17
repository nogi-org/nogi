package kr.co.nogibackend.infra.notice.adapter;

import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.port.NoticeCreateRepositoryPort;
import kr.co.nogibackend.infra.notice.jpa.NoticeCreateJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeCreateRepositoryPortAdapter implements NoticeCreateRepositoryPort {

	private final NoticeCreateJpa noticeCreateJpa;

	@Override
	public Notice create(Notice notice) {
		return noticeCreateJpa.save(notice);
	}

}
