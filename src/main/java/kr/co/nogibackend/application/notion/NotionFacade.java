package kr.co.nogibackend.application.notion;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.nogibackend.domain.github.GithubService;
import kr.co.nogibackend.domain.github.dto.command.GithubCommitCommand;
import kr.co.nogibackend.domain.notion.NotionService;
import kr.co.nogibackend.domain.notion.dto.command.NotionStartTILCommand;
import kr.co.nogibackend.domain.notion.dto.result.NotionStartTILResult;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.domain.user.dto.command.UserCheckTILCommand;
import kr.co.nogibackend.domain.user.dto.result.UserCheckTILResult;
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
	private final UserService userService;
	private final GithubService githubService;

	public void onNogi() {
		// 1. findUser: 유저의 역할은 현재 시간에 nogi 등록한 유저들 정보 가져오기
		// 2. startTIL: notion의 역할은 유저들의 TIL을 가져와서 마크다운으로 가공하기
		List<NotionStartTILResult> notionStartTILResults = notionService.startTIL(
			NotionStartTILCommand.builder().build());

		// 3. checkTIL: 유저 정보로 TIL이 생성인지, 수정인지 체크
		List<UserCheckTILCommand> userCheckTILCommands = notionStartTILResults.stream()
			.map(UserCheckTILCommand::from)
			.toList();
		List<UserCheckTILResult> userCheckTILResults = userService.checkTIL(userCheckTILCommands);

		// 4. onCommit: github의 역할은 마크다운을 유저들의 레파지토리에 커밋하기
		List<GithubCommitCommand> githubCommitCommands = GithubCommitCommand.of(notionStartTILResults,
			userCheckTILResults);
		githubService.commitToGithub(githubCommitCommands);

		// 5. endTIL: commit 성공과 실패를 notion 상태값 변경하기
		// 6. notify: 알림 역할은 nogi 결과를 알리기(유저 이메일, 노션 제목, 성공 또는 실패와 이유)
	}
}
