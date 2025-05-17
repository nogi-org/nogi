package kr.co.nogibackend.domain.user.service;

import static kr.co.nogibackend.global.response.code.UserResponseCode.F_NOT_FOUND_USER;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import kr.co.nogibackend.domain.notifier.port.NotifierPort;
import kr.co.nogibackend.domain.sync.command.UserStoreSyncHistoryCommand;
import kr.co.nogibackend.domain.sync.constant.SyncHistoryType;
import kr.co.nogibackend.domain.sync.entity.SyncHistory;
import kr.co.nogibackend.domain.sync.result.UserStoreSyncHistoryResult;
import kr.co.nogibackend.domain.user.command.UserCheckTILCommand;
import kr.co.nogibackend.domain.user.command.UserUpdateCommand;
import kr.co.nogibackend.domain.user.entity.User;
import kr.co.nogibackend.domain.user.entity.User.Role;
import kr.co.nogibackend.domain.user.port.UserRepositoryPort;
import kr.co.nogibackend.domain.user.result.UserCheckTILResult;
import kr.co.nogibackend.domain.user.result.UserReslut;
import kr.co.nogibackend.domain.user.result.UserResult;
import kr.co.nogibackend.domain.user.result.UserSinUpOrUpdateResult;
import kr.co.nogibackend.global.config.context.ExecutionResultContext;
import kr.co.nogibackend.global.config.exception.GlobalException;
import kr.co.nogibackend.interfaces.user.response.UserAccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepositoryPort userRepositoryPort;
	private final NotifierPort notifierPort;

	public List<UserCheckTILResult> checkTIL(List<UserCheckTILCommand> commands) {

		Long[] userIds = commands.stream().map(UserCheckTILCommand::userId).toArray(Long[]::new);
		List<String> notionPageIds = commands.stream().map(UserCheckTILCommand::notionPageId).toList();

		Map<Long, User> userMap = userRepositoryPort.findAllUserByIds(userIds)
				.stream()
				.collect(Collectors.toMap(User::getId, v -> v));
		Map<String, SyncHistory> nogiHistoryMap = userRepositoryPort.findAllNogiHistoryByNotionPageIds(
						notionPageIds)
				.stream()
				.collect(Collectors.toMap(SyncHistory::getNotionPageId, v -> v));

		return commands.stream()
				.map(command -> checkTIL(command, userMap, nogiHistoryMap))
				.flatMap(Optional::stream)
				.toList();
	}

	private Optional<UserCheckTILResult> checkTIL(
			UserCheckTILCommand command,
			Map<Long, User> userMap,
			Map<String, SyncHistory> nogiHistoryMap
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
		SyncHistory syncHistory = nogiHistoryMap.get(command.notionPageId());

		return Optional.ofNullable(this.checkTIL(command, user, syncHistory));
	}

	// Webhook 으로 단건 처리할 때를 위해 public 사용
	public UserCheckTILResult checkTIL(
			UserCheckTILCommand command,
			User user,
			SyncHistory syncHistory
	) {
		if (syncHistory == null) {
			// 이전 기록이 없으면 새로운 생성 (CREATE_OR_UPDATE_CONTENT)
			return UserCheckTILResult.of(
					command.userId(),
					user.getGithubOwner(),
					user.getGithubRepository(),
					user.getGithubDefaultBranch(),
					user.getGithubEmail(),
					command.notionPageId(),
					user.getNotionAccessToken(),
					SyncHistoryType.CREATE_OR_UPDATE_CONTENT,
					command.category(),
					command.title(),
					user.getGithubAuthToken(),
					true
			);
		}

		SyncHistoryType historyType = null;
		if (!syncHistory.getCategory().equals(command.category())) {
			historyType = SyncHistoryType.UPDATE_CATEGORY;
		} else if (!syncHistory.getTitle().equals(command.title())) {
			historyType = SyncHistoryType.UPDATE_TITLE;
		}

		return UserCheckTILResult.of(
				command.userId(),
				user.getGithubOwner(),
				user.getGithubRepository(),
				user.getGithubDefaultBranch(),
				user.getGithubEmail(),
				command.notionPageId(),
				user.getNotionAccessToken(),
				historyType,
				command.category(),
				command.title(),
				syncHistory.getCategory(),
				syncHistory.getTitle(),
				user.getGithubAuthToken(),
				true
		);
	}

	@Transactional
	public List<UserStoreSyncHistoryResult> storeNogiHistory(
			List<UserStoreSyncHistoryCommand> commands) {
		// key : notionPageId, value : NogiHistory
		Map<String, SyncHistory> nogiHistoryMap = userRepositoryPort.findAllNogiHistoryByNotionPageIds(
				commands.stream().map(UserStoreSyncHistoryCommand::notionPageId).toList()
		).stream().collect(Collectors.toMap(
						SyncHistory::getNotionPageId,
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
							return new UserStoreSyncHistoryResult(command.notionPageId());
						}
				).toList();
	}

	public boolean storeNogiHistory(UserStoreSyncHistoryCommand command,
			Map<String, SyncHistory> nogiHistoryMap) {
		try {
			if (nogiHistoryMap.containsKey(command.notionPageId())) {
				updateNogiHistory(nogiHistoryMap.get(command.notionPageId()), command);
				return true;
			} else {
				userRepositoryPort.saveNogiHistory(
						SyncHistory.builder()
								.user(User.builder().id(command.userId()).build())
								.notionPageId(command.notionPageId())
								.category(command.category())
								.title(command.title())
								.content(command.content())
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

	public void updateNogiHistory(SyncHistory syncHistory, UserStoreSyncHistoryCommand command) {
		syncHistory.updateMarkdownInfo(
				command.category(),
				command.title(),
				command.content()
		);
	}

	public List<UserResult> getUsersByIds(Long... userIds) {
		return userRepositoryPort.findAllUserByIds(userIds)
				.stream()
				.map(UserResult::from)
				.toList();
	}

	public List<UserResult> findUser() {
		return userRepositoryPort.findAllUser().stream()
				.map(UserResult::from)
				.toList();
	}

	public Optional<UserResult> findNogiBot() {
		Optional<User> masterUser = userRepositoryPort.findNogiBot();
		return masterUser.map(UserResult::from);
	}

	@Transactional
	public UserSinUpOrUpdateResult signUpOrUpdateUser(Long githubId, UserUpdateCommand command) {
		AtomicBoolean isSinUp = new AtomicBoolean(false);
		User user = userRepositoryPort.findByGithubId(githubId)
				.map(existingUser -> {
					// access token update
					existingUser.update(
							UserUpdateCommand.builder()
									.githubAuthToken(command.getGithubAuthToken())
									.build()
					);
					isSinUp.set(false);
					return existingUser;
				})
				.orElseGet(() -> {
					// create new user
					User newUser =
							User.builder()
									.role(Role.USER)
									.githubId(githubId)
									.githubAuthToken(command.getGithubAuthToken())
									.githubEmail(command.getGithubEmail())
									.githubOwner(command.getGithubOwner())
									.isNotificationAllowed(true)
									.build();
					isSinUp.set(true);
					return userRepositoryPort.saveUser(newUser);
				});

		return UserSinUpOrUpdateResult.from(user, isSinUp.get());
	}

	@Transactional
	public UserReslut updateUser(UserUpdateCommand command) {
		User updateTarget = userRepositoryPort.findById(command.getId())
				.orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));

		return UserReslut.from(updateTarget.update(command));
	}

	public UserReslut findUserById(Long id) {
		User user = userRepositoryPort.findById(id)
				.orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));
		return UserReslut.from(user);
	}

	public UserResult findUserByIdForFacade(Long id) {
		User user =
				userRepositoryPort
						.findById(id)
						.orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));
		return UserResult.from(user);
	}

	@Async
	public void sendSinUpNotification(Long userId, String ownerName) {
		// todo: 여기서 메세지를 만들어서 payload에 맞게 넘기기
		notifierPort.send(userId, ownerName);
	}

	public List<UserAccountResponse> getUsers() {
		return UserAccountResponse.from(userRepositoryPort.findAll());
	}

}
