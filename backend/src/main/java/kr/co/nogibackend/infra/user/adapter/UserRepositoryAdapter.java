package kr.co.nogibackend.infra.user.adapter;

import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.sync.entity.SyncHistory;
import kr.co.nogibackend.domain.user.entity.User;
import kr.co.nogibackend.domain.user.port.UserRepositoryPort;
import kr.co.nogibackend.infra.sync.jpa.SyncHistoryJpa;
import kr.co.nogibackend.infra.user.jpa.UserJpa;
import kr.co.nogibackend.infra.user.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

	private final UserQuery userQuery;
	private final UserJpa userJpa;
	private final SyncHistoryJpa syncHistoryJpa;

	@Override
	public List<User> findAllUserByIds(Long... userIds) {
		return userQuery.findAllUserByIds(userIds);
	}

	@Override
	public List<SyncHistory> findAllNogiHistoryByNotionPageIds(List<String> notionPageIds) {
		return userQuery.findAllNogiHistoryByNotionPageIds(notionPageIds);
	}

	@Override
	public SyncHistory saveNogiHistory(SyncHistory syncHistory) {
		return syncHistoryJpa.save(syncHistory);
	}

	@Override
	public List<User> findAllUser() {
		return userQuery.findAllUser();
	}

	@Override
	public Optional<User> findNogiBot() {
		return userQuery.findNogiBot();
	}

	@Override
	public Optional<User> findByGithubId(Long githubId) {
		return userQuery.findByGithubId(githubId);
	}

	@Override
	public User saveUser(User user) {
		return userJpa.save(user);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userJpa.findById(id);
	}

	@Override
	public List<User> findAll() {
		return userJpa.findAll();
	}

}
