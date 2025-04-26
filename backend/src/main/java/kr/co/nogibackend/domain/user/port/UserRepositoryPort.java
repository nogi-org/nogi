package kr.co.nogibackend.domain.user.port;

import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.sync.entity.SyncHistory;
import kr.co.nogibackend.domain.user.entity.User;

public interface UserRepositoryPort {

	List<User> findAllUserByIds(Long... userIds);

	List<SyncHistory> findAllNogiHistoryByNotionPageIds(List<String> notionPageIds);

	SyncHistory saveNogiHistory(SyncHistory build);

	List<User> findAllUser();

	Optional<User> findNogiBot();

	Optional<User> findByGithubId(Long githubId);

	User saveUser(User user);

	Optional<User> findById(Long id);

	List<User> findAll();

}
