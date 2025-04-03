package kr.co.nogibackend.domain.notice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kr.co.nogibackend.domain.admin.dto.command.NotionNoticeCreateCommand;
import kr.co.nogibackend.domain.admin.dto.request.NotionCreateNoticeRequest;
import kr.co.nogibackend.domain.notice.entity.Notice;
import kr.co.nogibackend.domain.notice.repository.NoticeCreateRepository;
import kr.co.nogibackend.domain.notion.NotionClient;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeCreateService {

	private final UserRepository userRepository;
	private final NoticeCreateRepository noticeCreateRepository;
	private final NotionClient notionClient;

	// todo: 시큐리티 적용하기
	@Transactional
	public Map<String, List<String>> createNotice(NotionNoticeCreateCommand command) {
		List<User> users = this.getUsersFilterNotionTokenAndNotionDatabaseId();
		Notice notice = this.saveNewNotice(command);
		this.createNewNoticeToNotion(users, notice);


    /*
      1. 특정 공지사항을 어떤 유저가 받았는지 히스토리 필요
      * 유저 -> 유저_공지사항 <- 공지사항
       */
		// todo: 공지사항 히스토리에 저장하기() try,catch 잡아서 실패 시 로그 남기기

		// todo: 응답을 어덯게 해주는게 좋을지 생각해보기
		return null;
	}

	private void createNewNoticeToNotion(List<User> users, Notice notice) {
		List<User> success = new ArrayList<>();
		List<User> fail = new ArrayList<>();
		List<NotionBlockInfo> content = notice.createContent();

		for (User user : users) {
			try {
				NotionCreateNoticeRequest request =
						NotionCreateNoticeRequest.ofNotice(
								user.getNotionDatabaseId()
								, notice.getTitle()
								, content
						);

				notionClient.createPage(user.getNotionAccessToken(), request);
				success.add(user);
			} catch (Exception error) {
				log.info("[NoticeCreateService] createNewNoticeToNotion - Error : {} ", error.getMessage());
				fail.add(user);
			}
		}
	}

	private List<User> getUsersFilterNotionTokenAndNotionDatabaseId() {
		return
				userRepository
						.findAllUser()
						.stream()
						.filter(user -> user.hasNotionAccessToken() && user.hasNotionDatabaseId())
						.toList();
	}

	private Notice saveNewNotice(NotionNoticeCreateCommand command) {
		return noticeCreateRepository.create(command.buildNotice());
	}

}
