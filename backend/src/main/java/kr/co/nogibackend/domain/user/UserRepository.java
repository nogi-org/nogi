package kr.co.nogibackend.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

	List<User> findAllUserByIds(Long... userIds);

	List<NogiHistory> findAllNogiHistoryByNotionPageIds(List<String> notionPageIds);

	NogiHistory saveNogiHistory(NogiHistory build);

	List<User> findAllUser();

	Optional<User> findNogiBot();

	Optional<User> findByGithubId(Long githubId);

	User saveUser(User user);

	Optional<User> findById(Long id);

	List<User> findAll();

}
