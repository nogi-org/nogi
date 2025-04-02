package kr.co.nogibackend.domain.notice.repository;

import kr.co.nogibackend.domain.notice.entity.Notice;

public interface NoticeCreateRepository {

	Notice create(Notice notice);

}
