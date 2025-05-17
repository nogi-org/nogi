package kr.co.nogibackend.infra.notice.adapter;

import java.util.List;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.port.NoticeUserCreateRepositoryPort;
import kr.co.nogibackend.infra.notice.jpa.NoticeUserCreateJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeUserCreateRepositoryPortAdapter implements NoticeUserCreateRepositoryPort {

	private final NoticeUserCreateJpa noticeUserCreateJpa;

	@Override
	public List<NoticeUser> saveAll(List<NoticeUser> noticeUser) {
		return noticeUserCreateJpa.saveAll(noticeUser);
	}

}
