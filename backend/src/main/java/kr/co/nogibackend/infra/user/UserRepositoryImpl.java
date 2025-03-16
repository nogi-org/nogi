package kr.co.nogibackend.infra.user;

import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.user.NogiHistory;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserQueryRepository userQueryRepository;
	private final UserJpaRepository userJpaRepository;
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

	@Override
	public List<User> findAllUser() {
		return userQueryRepository.findAllUser();
	}

	@Override
	public Optional<User> findNogiBot() {
		return userQueryRepository.findNogiBot();
	}

	@Override
	public Optional<User> findByGithubId(Long githubId) {
		return userQueryRepository.findByGithubId(githubId);
	}

	@Override
	public User saveUser(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userJpaRepository.findById(id);
	}

	@Override
	public List<User> findAll() {
		return userJpaRepository.findAll();
	}

}
