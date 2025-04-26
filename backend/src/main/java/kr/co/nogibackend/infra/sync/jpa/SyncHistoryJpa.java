package kr.co.nogibackend.infra.sync.jpa;

import kr.co.nogibackend.domain.sync.entity.SyncHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncHistoryJpa extends JpaRepository<SyncHistory, Long> {

}
