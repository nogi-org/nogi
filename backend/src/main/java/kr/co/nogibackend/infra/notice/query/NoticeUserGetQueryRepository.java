package kr.co.nogibackend.infra.notice.query;

import static kr.co.nogibackend.domain.notice.entity.QNoticeUser.noticeUser;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeUserGetQueryRepository {

  private final JPAQueryFactory query;

  public List<NoticeUser> findByNoticeIdWithUserAndNotice(Long noticeId) {
    return
        query
            .select(noticeUser)
            .from(noticeUser)
            .where(noticeUser.notice.id.eq(noticeId))
            .fetch();
  }

}
