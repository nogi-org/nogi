package kr.co.nogibackend.domain.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kr.co.nogibackend.domain.admin.dto.command.NotionNoticeCreateCommand;
import kr.co.nogibackend.domain.admin.dto.request.NotionCreateNoticeRequest;
import kr.co.nogibackend.domain.notion.NotionClient;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminNoticeService {

	private final UserRepository userRepository;
	private final NotionClient notionClient;

	@Transactional
	public Map<String, List<String>> createNotice(NotionNoticeCreateCommand command) {
		List<User> users =
				userRepository
						.findAllUser()
						.stream()
						.filter(user -> user.hasNotionAccessToken() && user.hasNotionDatabaseId())
						.toList();

		// todo: 공지사항 db저장

		// 신규 노션 공지사항 등록
		List<User> successUser = new ArrayList<>();
		List<User> failUser = new ArrayList<>();
		for (User user : users) {
			try {
				NotionCreateNoticeRequest request =
						NotionCreateNoticeRequest.ofNotice(
								user.getNotionDatabaseId()
								, command.title()
								, command.createContent()
						);

				notionClient.createPage(user.getNotionAccessToken(), request);
				successUser.add(user);

				// todo: 유저_공지사항 db저장(덤프 save 필요)
			} catch (Exception error) {
				failUser.add(user);
			}
		}

    /*
      1. 특정 공지사항을 어떤 유저가 받았는지 히스토리 필요
      * 유저 -> 유저_공지사항 <- 공지사항
       */
		// todo: 공지사항 히스토리에 저장하기() try,catch 잡아서 실패 시 로그 남기기

		// todo: 응답을 어덯게 해주는게 좋을지 생각해보기
		return null;
	}

}
