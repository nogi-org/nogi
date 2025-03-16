package kr.co.nogibackend.infra.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.user.NogiHistory;
import kr.co.nogibackend.domain.user.QNogiHistory;
import kr.co.nogibackend.domain.user.QUser;
import kr.co.nogibackend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
            .where(qUser.role.eq(User.Role.NOGI_BOT))
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
