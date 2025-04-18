package kr.co.nogibackend.domain.notion.service;

import static kr.co.nogibackend.response.code.UserResponseCode.F_NOT_FOUND_USER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.notion.feignclient.NotionClient;
import kr.co.nogibackend.domain.notion.helper.NotionDataProvider;
import kr.co.nogibackend.domain.notion.helper.NotionRequestMaker;
import kr.co.nogibackend.domain.notion.info.NotionBaseInfo;
import kr.co.nogibackend.domain.notion.info.NotionBlockInfo;
import kr.co.nogibackend.domain.notion.info.NotionPageInfo;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.UserRepository;
import kr.co.nogibackend.interfaces.notion.dto.response.NotionUserPagesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotionUserPageService {

  private final NotionDataProvider notionDataProvider;
  private final NotionClient notionClient;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;

  public NotionUserPagesResponse getPages(Long userId, String nextCursor) {
    User user = this.findUser(userId);
    NotionBaseInfo<NotionPageInfo> pages =
        notionClient.getPagesFromDatabase(
            user.getNotionAccessToken()
            , user.getNotionDatabaseId()
            , NotionRequestMaker.createPageAllFilter(nextCursor)
        );
    return NotionUserPagesResponse.of(pages);
  }

  // todo: objectMapper 예외 처리 다시 하기
  public String getPage(Long userId, String pageId) {
    User user = this.findUser(userId);
    NotionBaseInfo<NotionBlockInfo> blocks =
        notionDataProvider
            .getBlocks(user.getNotionAccessToken(), pageId);
    notionDataProvider.preprocessMarkdown(blocks, user.getNotionAccessToken());

    try {
      return objectMapper.writeValueAsString(blocks);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private User findUser(Long userId) {
    return
        userRepository
            .findById(userId)
            .orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));
  }

}
