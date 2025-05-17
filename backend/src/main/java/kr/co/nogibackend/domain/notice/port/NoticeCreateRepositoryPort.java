package kr.co.nogibackend.domain.notice.port;

import kr.co.nogibackend.domain.notice.entity.Notice;

public interface NoticeCreateRepositoryPort {

	Notice create(Notice notice);

}
