package kr.co.nogibackend.infra.notice.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeCreateQuery {

	private final JPAQueryFactory query;
}
