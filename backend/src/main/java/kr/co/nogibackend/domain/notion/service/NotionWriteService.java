package kr.co.nogibackend.domain.notion.service;

import java.util.List;
import java.util.Optional;
import kr.co.nogibackend.domain.notion.dto.command.NotionEndTILCommand;
import kr.co.nogibackend.domain.notion.dto.result.NotionEndTILResult;
import kr.co.nogibackend.domain.notion.helper.NotionDataInjector;
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
public class NotionWriteService {

  private final NotionDataInjector notionDataInjector;

  // Github 에 commit 된 결과를 notion 상태값 변경
  public List<NotionEndTILResult> updateStatusByResult(List<NotionEndTILCommand> commands) {
    return
        commands
            .stream()
            .map(this::updateStatusByResult)
            .flatMap(Optional::stream)
            .toList();
  }

  /**
   * <h2>✅ Notion TIL 상태 업데이트</h2>
   *
   * <ul>
   *   <li>1️⃣ TIL 커밋 성공/실패 여부를 기반으로 상태 업데이트</li>
   *   <li>2️⃣ Notion 페이지 ID, 사용자 ID를 이용해 상태 변경</li>
   *   <li>3️⃣ 성공한 경우 NotionEndTILResult 객체 생성</li>
   *   <li>4️⃣ 실패한 경우 Optional.empty() 반환</li>
   * </ul>
   */
  public Optional<NotionEndTILResult> updateStatusByResult(NotionEndTILCommand command) {
    // 1️⃣ TIL 커밋 성공/실패 여부를 기반으로 상태 업데이트
    boolean isUpdateResult =
        notionDataInjector.updateTILResultStatus(command.isSuccess(), command.notionBotToken(),
            command.notionPageId(),
            command.userId());

    // 2️⃣ Notion 페이지 ID, 사용자 ID를 이용해 상태 변경
    return
        isUpdateResult && command.isSuccess()
            ? Optional.of(
            // 3️⃣ 성공한 경우 NotionEndTILResult 객체 생성
            new NotionEndTILResult(
                command.userId(),
                command.notionPageId(),
                command.category(),
                command.title(),
                command.content()
            ))
            // 4️⃣ 실패한 경우 Optional.empty() 반환
            : Optional.empty();
  }

}
