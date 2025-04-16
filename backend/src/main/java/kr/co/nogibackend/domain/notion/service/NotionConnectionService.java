package kr.co.nogibackend.domain.notion.service;

import static kr.co.nogibackend.response.code.UserResponseCode.F_NOT_FOUND_USER;

import java.util.Optional;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.notion.NotionClient;
import kr.co.nogibackend.domain.notion.dto.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.helper.NotionDataProvider;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.UserRepository;
import kr.co.nogibackend.interfaces.notion.dto.response.NotionConnectionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
  노션 용어정리
  1. 데이터베이스: TIL 페이지를 담고있는 데이터베이스, 속성도 포함
  2. 페이지: 데이터베이스가 담고 있는 여러개의 페이지, 페이지는 각각 TIL 로 구분됨
  3. 블럭: 페이지에 작성된 내용, 한줄이 블럭 한개
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotionConnectionService {

  private final NotionDataProvider notionDataProvider;
  private final NotionClient notionClient;
  private final UserRepository userRepository;

  public NotionConnectionResponse onConnectionTest(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));

    // notion page id 없으면 연결 실패
    if (user.isEmptyNotionPageId()) {
      return new NotionConnectionResponse(false, "Notion Page ID가 없어요.");
    }

    // notion database id 가 없는 경우 page id 로 조회 후 체크
    // 처음 회원가입 후 노션 데이터베이서 id가 없을 수 있음.
    if (user.isEmptyNotionDatabaseId()) {
      Optional<NotionBlockInfo> database =
          notionDataProvider
              .getNotionDatabaseByPageId(user.getNotionAccessToken(), user.getNotionPageId());
      
      return database.isPresent()
          ? new NotionConnectionResponse(true, "연결 확인되었어요.")
          : new NotionConnectionResponse(false, "Notion 페이지에서 Database를 조회할 수 없어요.");
    }

    // notion database id 가 있는 경우 access token 으로 조회 후 체크
    try {
      notionClient.getDatabase(user.getNotionAccessToken(), user.getNotionDatabaseId());
      return new NotionConnectionResponse(true, "연결 확인되었어요.");
    } catch (Exception error) {
      return new NotionConnectionResponse(false, "Notion Access 정보로 Database를 조회할 수 없어요.");
    }
  }

}
