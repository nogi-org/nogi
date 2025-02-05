package kr.co.nogibackend.infra.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.co.nogibackend.domain.user.NogiHistory;
import kr.co.nogibackend.domain.user.QNogiHistory;
import kr.co.nogibackend.domain.user.QUser;
import kr.co.nogibackend.domain.user.User;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<NogiHistory> findAllNogiHistoryByNotionPageIds(List<String> notionPageId) {

		var qNogiHistory = QNogiHistory.nogiHistory;

		return queryFactory.selectFrom(qNogiHistory)
			.where(qNogiHistory.notionPageId.in(notionPageId))
			.fetch();
	}

	public List<User> findAllUserByIds(List<Long> userIds) {

		QUser qUser = QUser.user;

		return queryFactory.selectFrom(qUser)
			.where(qUser.id.in(userIds))
			.fetch();
	}
}
