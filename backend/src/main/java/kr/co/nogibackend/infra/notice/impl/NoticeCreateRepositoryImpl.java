package kr.co.nogibackend.infra.notice.impl;

import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.repository.NoticeCreateRepository;
import kr.co.nogibackend.infra.notice.jpa.NoticeCreateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeCreateRepositoryImpl implements NoticeCreateRepository {

	private final NoticeCreateJpaRepository noticeCreateJpaRepository;

	@Override
	public Notice create(Notice notice) {
		return noticeCreateJpaRepository.save(notice);
	}

}
