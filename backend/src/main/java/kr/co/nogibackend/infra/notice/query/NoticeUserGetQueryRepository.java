package kr.co.nogibackend.infra.notice.query;

import static kr.co.nogibackend.domain.notice.entity.QNoticeUser.noticeUser;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.co.nogibackend.domain.notice.dto.condition.NoticeUserSearchConditions;
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

  /**
   * <h1>단순 복수 조회, 복수 조건</h1>
   */
  public List<NoticeUser> searchByConditions(NoticeUserSearchConditions conditions) {
    return
        query
            .select(noticeUser)
            .from(noticeUser)
            .join(noticeUser.notice).fetchJoin()
            .join(noticeUser.user).fetchJoin()
            .where(
                this.eqId(conditions.noticeId())
                , this.eqIsSuccess(conditions.isSuccess())
            )
            .fetch();
  }

  // todo: 이거는 나중에 필요할 때 다시 사용하기
//  public Optional<NoticeUser> findByConditions(NoticeUserFindConditions conditions) {
//    return
//        Optional.of(
//            query
//                .select(noticeUser)
//                .from(noticeUser)
//                .join(noticeUser.notice).fetchJoin()
//                .join(noticeUser.user).fetchJoin()
//                .where(
//                    this.eqId(conditions.noticeId())
//                    , this.eqIsSuccess(conditions.isSuccess())
//                )
//                .fetchOne()
//        );
//  }

  public Page<NoticeUser> findRecipientsPage(
      Long noticeId
      // todo: conditions로 이름 바꾸기
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

  private BooleanExpression eqId(Long id) {
    return id == null ? null : noticeUser.id.eq(id);
  }

  private BooleanExpression eqIsSuccess(Boolean isSuccess) {
    return isSuccess == null ? null : noticeUser.isSuccess.eq(isSuccess);
  }

}
