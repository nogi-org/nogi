package kr.co.nogibackend.domain.notice.repository;

import java.util.List;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;

public interface NoticeUserCreateRepository {

  List<NoticeUser> saveAll(List<NoticeUser> noticeUser);

}
