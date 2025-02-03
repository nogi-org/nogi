package kr.co.nogibackend.domain.user;

import java.util.Optional;

public interface UserRepository {
	Optional<NogiHistory> findByUserIdAndNotionPageId(Long userId, String notionPageId);
}
