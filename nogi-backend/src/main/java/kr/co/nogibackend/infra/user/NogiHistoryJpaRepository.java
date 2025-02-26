package kr.co.nogibackend.infra.user;

import kr.co.nogibackend.domain.user.NogiHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NogiHistoryJpaRepository extends JpaRepository<NogiHistory, Long> {

}
