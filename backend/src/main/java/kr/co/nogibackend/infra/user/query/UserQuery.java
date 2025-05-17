package kr.co.nogibackend.infra.user.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.sync.entity.QSyncHistory;
import kr.co.nogibackend.domain.sync.entity.SyncHistory;
import kr.co.nogibackend.domain.user.entity.QUser;
import kr.co.nogibackend.domain.user.entity.User;
import kr.co.nogibackend.domain.user.entity.User.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQuery {

	private final JPAQueryFactory queryFactory;

	public List<SyncHistory> findAllNogiHistoryByNotionPageIds(List<String> notionPageId) {
		var qSyncHistory = QSyncHistory.syncHistory;

		return queryFactory.selectFrom(qSyncHistory)
				.where(qSyncHistory.notionPageId.in(notionPageId))
				.fetch();
	}

	public List<User> findAllUserByIds(Long... userIds) {
		QUser qUser = QUser.user;

		return queryFactory.selectFrom(qUser)
				.where(qUser.id.in(userIds))
				.fetch();
	}

	public List<User> findAllUser() {
		QUser qUser = QUser.user;

		return queryFactory.selectFrom(qUser)
				.where(qUser.role.eq(User.Role.USER))
				.fetch();
	}

	public Optional<User> findNogiBot() {
		QUser qUser = QUser.user;

		return Optional.ofNullable(
				queryFactory.selectFrom(qUser)
						.where(qUser.role.eq(Role.ADMIN))
						.fetchOne()
		);
	}

	public Optional<User> findByGithubId(Long githubId) {
		QUser qUser = QUser.user;

		return Optional.ofNullable(
				queryFactory.selectFrom(qUser)
						.where(
								qUser.githubId.eq(githubId)
						)
						.fetchOne()
		);
	}
}
