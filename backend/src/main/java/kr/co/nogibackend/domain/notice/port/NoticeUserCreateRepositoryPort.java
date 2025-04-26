package kr.co.nogibackend.domain.notice.port;

import java.util.List;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;

public interface NoticeUserCreateRepositoryPort {

	List<NoticeUser> saveAll(List<NoticeUser> noticeUser);

}
