package kr.co.nogibackend.domain.admin;

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
    /*
    2. 공지사항 db저장
     */

    // 신규 노션 공지사항 등록
    for (User user : users) {
      NotionCreateNoticeRequest request =
          new NotionCreateNoticeRequest()
              .of(
                  user.getNotionDatabaseId(),
                  command.title(),
                  command.createContent()
              );
      notionClient.createPage(user.getNotionAccessToken(), request);
    }

    return null;
  }

}
