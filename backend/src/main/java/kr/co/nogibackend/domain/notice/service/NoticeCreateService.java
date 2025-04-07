package kr.co.nogibackend.domain.notice.service;

import jakarta.persistence.EntityManager;
import kr.co.nogibackend.application.notice.dto.NoticePublishCommand;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.helper.NoticeDataSaver;
import kr.co.nogibackend.domain.notice.repository.NoticeCreateRepository;
import kr.co.nogibackend.domain.notion.helper.NotionDataInjector;
import kr.co.nogibackend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeCreateService {

	private final UserRepository userRepository;
	private final NoticeCreateRepository noticeCreateRepository;
	private final NotionDataInjector notionDataInjector;
	private final NoticeDataSaver noticeDataSaver;
	private final EntityManager entityManager;

	public Notice create(NoticePublishCommand command) {
		Notice notice = noticeCreateRepository.create(command.buildInitCreateNotice());
		log.info("[NoticeCreateService] create - "
						+ "isManaged?isManaged?isManaged?isManaged?isManaged?isManaged? : {} ",
				entityManager.contains(notice));
		notice.updateUrl();
		return notice;
	}

}
