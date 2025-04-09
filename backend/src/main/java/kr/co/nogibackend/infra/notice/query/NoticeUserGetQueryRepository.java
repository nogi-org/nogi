package kr.co.nogibackend.infra.notice.query;

import static kr.co.nogibackend.domain.notice.entity.QNoticeUser.noticeUser;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeUserGetQueryRepository {

  private final JPAQueryFactory query;

  public Page<NoticeUser> findRecipientsPage(
      Long noticeId
      , NoticeRecipientsRequest request
      , Pageable pageable
  ) {
    List<NoticeUser> content =
        query
            .select(noticeUser)
            .from(noticeUser)
            .join(noticeUser.notice).fetchJoin()
            .join(noticeUser.user).fetchJoin()
            .where(
                noticeUser.notice.id.eq(noticeId)
                , this.eqIsSuccess(request.isSuccess())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(noticeUser.notice.id.desc())
            .fetch();

    Long total =
        query
            .select(noticeUser.count())
            .from(noticeUser)
            .join(noticeUser.notice)
            .join(noticeUser.user)
            .where(
                noticeUser.notice.id.eq(noticeId)
                , this.eqIsSuccess(request.isSuccess())
            )
            .fetchOne();

    return new PageImpl<>(content, pageable, total);
  }

  private BooleanExpression eqIsSuccess(Boolean isSuccess) {
    return isSuccess == null ? null : noticeUser.isSuccess.eq(isSuccess);
  }

}
