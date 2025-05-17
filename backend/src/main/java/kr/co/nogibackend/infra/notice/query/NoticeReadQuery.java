package kr.co.nogibackend.infra.notice.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.entity.QNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeReadQuery {

	private final JPAQueryFactory query;

	public Page<Notice> getNotices(Pageable pageable) {
		QNotice notice = QNotice.notice;

		List<Notice> content =
				query
						.selectFrom(notice)
						.offset(pageable.getOffset())
						.limit(pageable.getPageSize())
						.orderBy(notice.id.desc())
						.fetch();

		Long total =
				query
						.select(notice.count())
						.from(notice)
						.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}

}
