package kr.co.nogibackend.infra.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.nogibackend.domain.user.NogiHistory;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserQueryRepository userQueryRepository;
	private final NogiHistoryJpaRepository nogiHistoryJpaRepository;

	@Override
	public List<User> findAllUserByIds(Long... userIds) {
		return userQueryRepository.findAllUserByIds(userIds);
	}

	@Override
	public List<NogiHistory> findAllNogiHistoryByNotionPageIds(List<String> notionPageIds) {
		return userQueryRepository.findAllNogiHistoryByNotionPageIds(notionPageIds);
	}

	@Override
	public NogiHistory saveNogiHistory(NogiHistory nogiHistory) {
		return nogiHistoryJpaRepository.save(nogiHistory);
	}
}
