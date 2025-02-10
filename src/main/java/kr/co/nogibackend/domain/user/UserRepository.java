package kr.co.nogibackend.domain.user;

import java.util.List;

public interface UserRepository {

	List<User> findAllUserByIds(Long... userIds);

	List<NogiHistory> findAllNogiHistoryByNotionPageIds(List<String> notionPageIds);

	NogiHistory saveNogiHistory(NogiHistory build);
}
