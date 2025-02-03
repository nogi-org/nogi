package kr.co.nogibackend.infra.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.co.nogibackend.domain.user.NogiHistory;
import kr.co.nogibackend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserQueryRepository userQueryRepository;

	@Override
	public Optional<NogiHistory> findByUserIdAndNotionPageId(Long userId, String notionPageId) {
		return userQueryRepository.findByUserIdAndNotionPageId(userId, notionPageId);
	}
}
