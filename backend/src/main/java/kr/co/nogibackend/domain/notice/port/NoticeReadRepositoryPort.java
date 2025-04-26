package kr.co.nogibackend.domain.notice.port;

import java.util.Optional;
import kr.co.nogibackend.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeReadRepositoryPort {

	Page<Notice> getNotices(Pageable pageable);

	Optional<Notice> findById(Long noticeId);

}
