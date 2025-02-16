package kr.co.nogibackend.domain.user;

import static kr.co.nogibackend.response.code.UserResponseCode.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nogibackend.config.context.ExecutionResultContext;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.domain.user.User.Role;
import kr.co.nogibackend.domain.user.dto.command.UserCheckTILCommand;
import kr.co.nogibackend.domain.user.dto.command.UserStoreNogiHistoryCommand;
import kr.co.nogibackend.domain.user.dto.command.UserUpdateCommand;
import kr.co.nogibackend.domain.user.dto.info.UserInfo;
import kr.co.nogibackend.domain.user.dto.result.UserCheckTILResult;
import kr.co.nogibackend.domain.user.dto.result.UserResult;
import kr.co.nogibackend.domain.user.dto.result.UserStoreNogiHistoryResult;
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

		Long[] userIds = commands.stream().map(UserCheckTILCommand::userId).toArray(Long[]::new);
		List<String> notionPageIds = commands.stream().map(UserCheckTILCommand::notionPageId).toList();

		Map<Long, User> userMap = userRepository.findAllUserByIds(userIds)
			.stream()
			.collect(Collectors.toMap(User::getId, v -> v));
		Map<String, NogiHistory> nogiHistoryMap = userRepository.findAllNogiHistoryByNotionPageIds(notionPageIds)
			.stream()
			.collect(Collectors.toMap(NogiHistory::getNotionPageId, v -> v));

		return commands.stream()
			.map(command -> checkTIL(command, userMap, nogiHistoryMap))
			.flatMap(Optional::stream)
			.toList();
	}

	private Optional<UserCheckTILResult> checkTIL(
		UserCheckTILCommand command,
		Map<Long, User> userMap,
		Map<String, NogiHistory> nogiHistoryMap
	) {
		// 유저 존재 여부 체크
		if (!userMap.containsKey(command.userId())) {
			ExecutionResultContext.addNotionPageErrorResult("User not found", command.userId());
			return Optional.empty();
		}

		// 유저 GithubAuthToken 존재 여부 체크
		User user = userMap.get(command.userId());
		if (user.getGithubAuthToken() == null) {
			ExecutionResultContext.addNotionPageErrorResult("User not found", command.userId());
			return Optional.empty();
		}

		// 이전 기록 조회 (notionPageId 기반)
		NogiHistory nogiHistory = nogiHistoryMap.get(command.notionPageId());

		return Optional.ofNullable(this.checkTIL(command, user, nogiHistory));
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
				user.getNotionAuthToken(),
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
			user.getNotionAuthToken(),
			historyType,
			command.category(),
			command.title(),
			nogiHistory.getCategory(),
			nogiHistory.getTitle(),
			user.getGithubAuthToken(),
			true
		);
	}

	public List<UserStoreNogiHistoryResult> storeNogiHistory(List<UserStoreNogiHistoryCommand> commands) {
		// key : notionPageId, value : NogiHistory
		Map<String, NogiHistory> nogiHistoryMap = userRepository.findAllNogiHistoryByNotionPageIds(
			commands.stream().map(UserStoreNogiHistoryCommand::notionPageId).toList()
		).stream().collect(Collectors.toMap(
				NogiHistory::getNotionPageId,
				v -> v
			)
		);

		return commands.stream()
			.filter(command -> storeNogiHistory(command, nogiHistoryMap)) // 성공한 경우만 필터링
			.map(command -> {
					// 성공한 경우 알림을 취해 결과를 추가
					ExecutionResultContext.addSuccessResult(
						"%s/%s.md를 성공적으로 커밋했어요!".formatted(command.category(), command.title()),
						command.userId()
					);
					return new UserStoreNogiHistoryResult(command.notionPageId());
				}
			).toList();
	}

	public boolean storeNogiHistory(UserStoreNogiHistoryCommand command, Map<String, NogiHistory> nogiHistoryMap) {
		try {
			if (nogiHistoryMap.containsKey(command.notionPageId())) {
				updateNogiHistory(nogiHistoryMap.get(command.notionPageId()), command);
				return true;
			} else {
				userRepository.saveNogiHistory(
					NogiHistory.builder()
						.user(User.builder().id(command.userId()).build())
						.notionPageId(command.notionPageId())
						.category(command.category())
						.title(command.title())
						.build()
				);
				return true;
			}
		} catch (Exception e) {
			ExecutionResultContext.addNotionPageErrorResult(
				"DB에 이력을 저장중 문제가 발생했어요",
				command.userId()
			);
			return false;
		}
	}

	@Transactional
	public void updateNogiHistory(NogiHistory nogiHistory, UserStoreNogiHistoryCommand command) {
		nogiHistory.updateMarkdownInfo(
			command.category(),
			command.title()
		);
	}

	public List<UserResult> getUsersByIds(Long... userIds) {
		return userRepository.findAllUserByIds(userIds)
			.stream()
			.map(UserResult::from)
			.toList();
	}

	public List<UserResult> findUser() {
		return userRepository.findAllUser().stream()
			.map(UserResult::from)
			.toList();
	}

	public Optional<UserResult> findNogiBot() {
		Optional<User> masterUser = userRepository.findNogiBot();
		return masterUser.map(UserResult::from);
	}

	@Transactional
	public UserResult createOrUpdateUser(UserUpdateCommand command) {
		User user = userRepository.findByGithubOwner(command.getGithubOwner())
			.map(existingUser -> {
				existingUser.update(command);
				return existingUser;
			})
			.orElseGet(() -> {
				User newUser =
					User.builder()
						.role(Role.USER)
						.notionAuthToken(command.getNotionAuthToken())
						.notionDatabaseId(command.getNotionDatabaseId())
						.githubAuthToken(command.getGithubAuthToken())
						.githubRepository(command.getGithubRepository())
						.githubDefaultBranch(command.getGithubDefaultBranch())
						.githubEmail(command.getGithubEmail())
						.githubOwner(command.getGithubOwner())
						.build();
				return userRepository.saveUser(newUser);
			});

		return UserResult.from(user);
	}

	@Transactional
	public UserInfo updateUser(UserUpdateCommand command) {
		User updateTarget = userRepository.findById(command.getId())
			.orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));

		return UserInfo.from(updateTarget.update(command));
	}

	public UserInfo findUserById(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));
		return UserInfo.from(user);
	}
}
