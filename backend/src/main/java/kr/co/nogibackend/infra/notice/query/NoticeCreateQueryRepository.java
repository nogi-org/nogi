package kr.co.nogibackend.infra.notice.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeCreateQueryRepository {

  private final JPAQueryFactory query;
}