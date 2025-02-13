package kr.co.nogibackend.infra.user;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.nogibackend.domain.user.NogiHistory;

public interface NogiHistoryJpaRepository extends JpaRepository<NogiHistory, Long> {
}
