package kr.co.nogibackend.infra.notice.jpa;

import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeUserCreateJpa extends JpaRepository<NoticeUser, Long> {

}
