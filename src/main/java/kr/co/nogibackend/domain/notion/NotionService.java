package kr.co.nogibackend.domain.notion;

import static kr.co.nogibackend.domain.notion.NotionPropertyValue.STATUS_PENDING;

import kr.co.nogibackend.interfaces.notion.NotionClient;
import kr.co.nogibackend.interfaces.notion.request.NotionRequestMaker;
import kr.co.nogibackend.interfaces.notion.response.NotionResponse;
import kr.co.nogibackend.interfaces.notion.response.NotionPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotionService {

  private final NotionClient notionClient;

  /*
  1. 유저정보 받기(노션 정보)
  2. 데이터베이스에 페이지 가져오기
  3. 페이지에서 블럭 가져오기
  4. 블럭을 가공하기
  5. 이미지 처리
  6. 넘기기
   */
  public NotionResponse<NotionPageResponse> demo(String authToken, String databaseId) {

    ResponseEntity<NotionResponse<NotionPageResponse>> databases =
        notionClient.getPagesFromDatabase(authToken, databaseId,
            NotionRequestMaker.filterStatusEq(STATUS_PENDING));

    System.out.println("databases ==================> " + databases.getBody().getObject());
    System.out.println("databases ==================> " + databases.getBody().getResults());
    System.out.println("databases ==================> " + databases.getBody().getNext_cursor());
    System.out.println("databases ==================> " + databases.getBody().getHas_more());
    System.out.println("databases ==================> " + databases.getBody().getType());
    return databases.getBody();
  }

}
