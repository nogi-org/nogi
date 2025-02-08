package kr.co.nogibackend.domain.user;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.co.nogibackend.domain.user.dto.command.UserCheckTILCommand;
import kr.co.nogibackend.domain.user.dto.result.UserCheckTILResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	/*
	➡️ Notion 에서 가져온 페이지 정보가 아래 3가지 중 어떤 케이스인지 체크
	1. 생성 또는 파일내용 : nogiHistory 에 notionPageId 가 없는 경우 or 있으나 category 와 title 은 그대로인 경우
	2. 파일제목 수정 : nogiHsitory 에 notionPageId 가 있고 category 는 그대로이면서 title이 변경된 경우
	3. 카테고리 수정 : nogiHistory 에 notionPageId 가 있고 category 가 변경된 경우
	 */
	public List<UserCheckTILResult> checkTIL(List<UserCheckTILCommand> commands) {

		List<Long> userIds = commands.stream().map(UserCheckTILCommand::userId).toList();
		List<String> notionPageIds = commands.stream().map(UserCheckTILCommand::notionPageId).toList();

		Map<Long, User> userMap = userRepository.findAllUserByIds(userIds)
			.stream()
			.collect(Collectors.toMap(User::getId, v -> v));
		Map<String, NogiHistory> nogiHistoryMap = userRepository.findAllNogiHistoryByNotionPageIds(notionPageIds)
			.stream()
			.collect(Collectors.toMap(NogiHistory::getNotionPageId, v -> v));

		return commands.stream().map(v -> this.checkTIL(v, userMap, nogiHistoryMap)).toList();
	}

	private UserCheckTILResult checkTIL(
		UserCheckTILCommand command,
		Map<Long, User> userMap,
		Map<String, NogiHistory> nogiHistoryMap
	) {
		// 유저 존재 여부 체크
		if (!userMap.containsKey(command.userId())) {
			return UserCheckTILResult.of(command.userId(), false);
		}

		// 유저 GithubAuthToken 존재 여부 체크
		User user = userMap.get(command.userId());
		if (user.getGithubAuthToken() == null) {
			return UserCheckTILResult.of(command.userId(), false);
		}

		// 이전 기록 조회 (notionPageId 기반)
		NogiHistory nogiHistory = nogiHistoryMap.get(command.notionPageId());

		return this.checkTIL(command, user, nogiHistory);
	}

	// Webhook 으로 단건 처리할 때를 위해 public 사용
	public UserCheckTILResult checkTIL(
		UserCheckTILCommand command,
		User user,
		NogiHistory nogiHistory
	) {
		if (nogiHistory == null) {
			// 이전 기록이 없으면 새로운 생성 (CREATE_OR_UPDATE_CONTENT)
			return UserCheckTILResult.of(
				command.userId(),
				user.getGithubOwner(),
				user.getGithubRepository(),
				user.getGithubDefaultBranch(),
				user.getGithubEmail(),
				command.notionPageId(),
				NogiHistoryType.CREATE_OR_UPDATE_CONTENT,
				command.category(),
				command.title(),
				user.getGithubAuthToken(),
				true
			);
		}

		NogiHistoryType historyType = null;
		if (!nogiHistory.getCategory().equals(command.category())) {
			historyType = NogiHistoryType.UPDATE_CATEGORY;
		} else if (!nogiHistory.getTitle().equals(command.title())) {
			historyType = NogiHistoryType.UPDATE_TITLE;
		}

		return UserCheckTILResult.of(
			command.userId(),
			user.getGithubOwner(),
			user.getGithubRepository(),
			user.getGithubDefaultBranch(),
			user.getGithubEmail(),
			command.notionPageId(),
			historyType,
			command.category(),
			command.title(),
			nogiHistory.getCategory(),
			nogiHistory.getTitle(),
			user.getGithubAuthToken(),
			true
		);
	}
}
