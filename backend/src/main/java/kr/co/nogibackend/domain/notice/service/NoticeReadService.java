package kr.co.nogibackend.domain.notice.service;

import static kr.co.nogibackend.global.response.code.NoticeResponseCode.F_NOT_FOUND_NOTICE;

import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.port.NoticeReadRepositoryPort;
import kr.co.nogibackend.domain.notice.port.NoticeUserReadRepositoryPort;
import kr.co.nogibackend.global.config.exception.GlobalException;
import kr.co.nogibackend.interfaces.notice.request.NoticeRecipientsRequest;
import kr.co.nogibackend.interfaces.notice.response.NoticeGetResponse;
import kr.co.nogibackend.interfaces.notice.response.NoticeRecipientsResponse;
import kr.co.nogibackend.interfaces.notice.response.NoticesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeReadService {

	private final NoticeReadRepositoryPort noticeReadRepositoryPort;
	private final NoticeUserReadRepositoryPort noticeUserReadRepositoryPort;

	public Page<NoticesResponse> getNotices(Pageable pageable) {
		return NoticesResponse.of(noticeReadRepositoryPort.getNotices(pageable));
	}

	public NoticeGetResponse getNotice(Long noticeId) {
		Notice notice =
				noticeReadRepositoryPort
						.findById(noticeId)
						.orElseThrow(() -> new GlobalException(F_NOT_FOUND_NOTICE));
		return NoticeGetResponse.of(notice);
	}

	@Transactional(readOnly = true)
	public Page<NoticeRecipientsResponse> getRecipients(
			Long noticeId
			, NoticeRecipientsRequest request
			, Pageable pageable
	) {
		Page<NoticeUser> noticeUsers =
				noticeUserReadRepositoryPort.findRecipientsPage(noticeId, request, pageable);
		return NoticeRecipientsResponse.of(noticeUsers);
	}

}
