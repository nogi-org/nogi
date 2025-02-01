package kr.co.nogibackend.application.notion;

import org.springframework.stereotype.Service;

import kr.co.nogibackend.domain.notion.NotionService;
import kr.co.nogibackend.domain.notion.dto.command.NotionStartTilCommand;
import lombok.RequiredArgsConstructor;

/*
  Package Name : kr.co.nogibackend.application.notion
  File Name    : NotionFacade
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Service
@RequiredArgsConstructor
public class NotionFacade {

	private final NotionService notionService;

	public void onNogi() {
		// 1. findUser: 유저의 역할은 현재 시간에 nogi 등록한 유저들 정보 가져오기
		// 2. startTIL: notion의 역할은 유저들의 TIL을 가져와서 마크다운으로 가공하기
		notionService.startTIL(NotionStartTilCommand.builder().build());
		// 3. onCommit: github의 역할은 마크다운을 유저들의 레파지토리에 커밋하기
		// 4. endTIL: commit 성공과 실패를 notion 상태값 변경하기
		// 5. notify: 알림 역할은 nogi 결과를 알리기
	}
}
