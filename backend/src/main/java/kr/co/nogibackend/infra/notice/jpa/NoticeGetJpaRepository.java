package kr.co.nogibackend.infra.notice.jpa;

import kr.co.nogibackend.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeGetJpaRepository extends JpaRepository<Notice, Long> {

}
