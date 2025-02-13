package kr.co.nogibackend.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

	List<User> findAllUserByIds(Long... userIds);

	List<NogiHistory> findAllNogiHistoryByNotionPageIds(List<String> notionPageIds);

	NogiHistory saveNogiHistory(NogiHistory build);

	List<User> findAllUser();

	Optional<User> findNogiBot();

	Optional<User> findByGithubAccessToken(String accessToken);

	User saveUser(User user);
}
