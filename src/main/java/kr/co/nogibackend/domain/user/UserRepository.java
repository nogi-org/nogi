package kr.co.nogibackend.domain.user;

import java.util.List;

public interface UserRepository {

	List<User> findAllUserByIds(List<Long> userIds);

	List<NogiHistory> findAllNogiHistoryByNotionPageIds(List<String> notionPageIds);
}
