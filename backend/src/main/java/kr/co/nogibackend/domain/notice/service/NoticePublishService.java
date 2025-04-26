package kr.co.nogibackend.domain.notice.service;

import java.util.List;
import kr.co.nogibackend.domain.notice.command.NoticeUserSearchCommand;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.entity.NoticeUser;
import kr.co.nogibackend.domain.notice.port.NoticeUserReadRepositoryPort;
import kr.co.nogibackend.domain.notice.result.PublishNewNoticeResult;
import kr.co.nogibackend.domain.notion.helper.NotionDataInjector;
import kr.co.nogibackend.domain.user.entity.User;
import kr.co.nogibackend.domain.user.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticePublishService {

	private final UserRepositoryPort userRepositoryPort;
	private final NotionDataInjector notionDataInjector;
	private final NoticeUserReadRepositoryPort noticeUserReadRepositoryPort;

	@Transactional
	public List<PublishNewNoticeResult> publishToNotion(Notice notice) {
		List<User> users = userRepositoryPort.findAllUser();
		return notionDataInjector.publishNewNotice(users, notice);
	}

	@Transactional
	public List<PublishNewNoticeResult> rePublish(Long noticeId) {
		List<NoticeUser> noticeUsers =
				noticeUserReadRepositoryPort
						.searchByConditions(new NoticeUserSearchCommand(noticeId, false));

		if (noticeUsers.isEmpty()) {
			return List.of();
		}

		Notice notice = noticeUsers.get(0).getNotice();
		List<User> users = noticeUsers.stream().map(NoticeUser::getUser).toList();

		List<PublishNewNoticeResult> results =
				notionDataInjector.publishNewNotice(users, notice);

		noticeUsers.forEach(noticeUser -> noticeUser.updateIsSuccess(results));
		return results;
	}

}
