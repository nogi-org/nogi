package kr.co.nogibackend.infra.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.co.nogibackend.domain.user.NogiHistory;
import kr.co.nogibackend.domain.user.QNogiHistory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Optional<NogiHistory> findByUserIdAndNotionPageId(Long userId, String notionPageId) {

		var qNogiHistory = QNogiHistory.nogiHistory;

		return Optional.ofNullable(
			jpaQueryFactory.selectFrom(qNogiHistory)
				.where(
					qNogiHistory.user.id.eq(userId),
					qNogiHistory.notionPageId.eq(notionPageId))
				.fetchOne()
		);
	}

}
