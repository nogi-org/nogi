package kr.co.nogibackend.infra.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.nogibackend.domain.user.NogiHistory;
import kr.co.nogibackend.domain.user.NogiHistoryType;
import kr.co.nogibackend.domain.user.User;
import kr.co.nogibackend.domain.user.UserRepository;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.domain.user.dto.command.UserCheckTILCommand;
import kr.co.nogibackend.domain.user.dto.result.UserCheckTILResult;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

	@Mock
	UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	@DisplayName("이전 이력이 없는 경우, CREATE_OR_UPDATE_CONTENT 타입으로 처리")
	void testCheckTIL_createOrUpdateContent_whenNoHistory() {
		// given
		Long userId = 1L;
		String notionPageId = "page-1";
		String category = "cat1";
		String title = "title1";
		UserCheckTILCommand command = new UserCheckTILCommand(userId, notionPageId, category, title);
		List<UserCheckTILCommand> commands = Collections.singletonList(command);

		User user = User.builder()
			.id(userId)
			.githubAuthToken("valid-token")
			.githubOwner("owner1")
			.githubRepository("repo1")
			.githubDefaultBranch("main")
			.githubEmail("owner1@example.com")
			.build();

		when(userRepository.findAllUserByIds(anyList()))
			.thenReturn(Collections.singletonList(user));
		when(userRepository.findAllNogiHistoryByNotionPageIds(anyList()))
			.thenReturn(Collections.emptyList());

		// when
		List<UserCheckTILResult> results = userService.checkTIL(commands);

		// then
		verify(userRepository, times(1)).findAllUserByIds(any());
		verify(userRepository, times(1)).findAllNogiHistoryByNotionPageIds(any());
		assertThat(results)
			.hasSize(1)
			.extracting(r -> tuple(
				r.userId(),
				r.type(),
				r.newCategory(),
				r.newTitle(),
				r.prevCategory(),
				r.prevTitle(),
				r.isSuccess()
			))
			.containsExactly(tuple(
				userId,
				NogiHistoryType.CREATE_OR_UPDATE_CONTENT,
				category,
				title,
				null,
				null,
				true
			));
	}

	@Test
	@DisplayName("이전 이력이 존재하되, category와 title이 동일한 경우, CREATE_OR_UPDATE_CONTENT 타입으로 처리")
	void testCheckTIL_createOrUpdateContent_whenHistoryExistsButSame() {
		// given
		Long userId = 1L;
		String notionPageId = "page-1";
		String category = "cat1";
		String title = "title1";
		UserCheckTILCommand command = new UserCheckTILCommand(userId, notionPageId, category, title);
		List<UserCheckTILCommand> commands = Collections.singletonList(command);

		User user = User.builder()
			.id(userId)
			.githubAuthToken("valid-token")
			.githubOwner("owner1")
			.githubRepository("repo1")
			.githubDefaultBranch("main")
			.githubEmail("owner1@example.com")
			.build();

		NogiHistory history = NogiHistory.builder()
			.id(10L)
			.user(user)
			.notionPageId(notionPageId)
			.category(category)
			.title(title)
			.build();

		when(userRepository.findAllUserByIds(anyList()))
			.thenReturn(Collections.singletonList(user));
		when(userRepository.findAllNogiHistoryByNotionPageIds(anyList()))
			.thenReturn(Collections.singletonList(history));

		// when
		List<UserCheckTILResult> results = userService.checkTIL(commands);

		// then
		verify(userRepository, times(1)).findAllUserByIds(any());
		verify(userRepository, times(1)).findAllNogiHistoryByNotionPageIds(any());
		assertThat(results)
			.hasSize(1)
			.extracting(r -> tuple(
				r.userId(),
				r.type(),
				r.newCategory(),
				r.newTitle(),
				r.prevCategory(),
				r.prevTitle(),
				r.isSuccess()
			))
			.containsExactly(tuple(
				userId,
				null,
				category,
				title,
				category,
				title,
				true
			));
	}

	@Test
	@DisplayName("이전 이력이 존재하되, title이 변경된 경우, UPDATE_TITLE 타입으로 처리")
	void testCheckTIL_updateTitle_whenTitleChanged() {
		// given
		Long userId = 1L;
		String notionPageId = "page-1";
		String category = "cat1";
		String oldTitle = "oldTitle";
		String newTitle = "newTitle";
		UserCheckTILCommand command = new UserCheckTILCommand(userId, notionPageId, category, newTitle);
		List<UserCheckTILCommand> commands = Collections.singletonList(command);

		User user = User.builder()
			.id(userId)
			.githubAuthToken("valid-token")
			.githubOwner("owner1")
			.githubRepository("repo1")
			.githubDefaultBranch("main")
			.githubEmail("owner1@example.com")
			.build();

		NogiHistory history = NogiHistory.builder()
			.id(10L)
			.user(user)
			.notionPageId(notionPageId)
			.category(category)
			.title(oldTitle)
			.build();

		when(userRepository.findAllUserByIds(anyList()))
			.thenReturn(Collections.singletonList(user));
		when(userRepository.findAllNogiHistoryByNotionPageIds(anyList()))
			.thenReturn(Collections.singletonList(history));

		// when
		List<UserCheckTILResult> results = userService.checkTIL(commands);

		// then
		verify(userRepository, times(1)).findAllUserByIds(any());
		verify(userRepository, times(1)).findAllNogiHistoryByNotionPageIds(any());
		assertThat(results)
			.hasSize(1)
			.extracting(r -> tuple(
				r.userId(),
				r.type(),
				r.newCategory(),
				r.newTitle(),
				r.prevCategory(),
				r.prevTitle(),
				r.isSuccess()
			))
			.containsExactly(tuple(
				userId,
				NogiHistoryType.UPDATE_TITLE,
				category,
				newTitle,
				category,
				oldTitle,
				true
			));
	}

	@Test
	@DisplayName("이전 이력이 존재하되, category가 변경된 경우, UPDATE_CATEGORY 타입으로 처리")
	void testCheckTIL_updateCategory_whenCategoryChanged() {
		// given
		Long userId = 1L;
		String notionPageId = "page-1";
		String oldCategory = "oldCat";
		String newCategory = "newCat";
		String title = "title1";
		UserCheckTILCommand command = new UserCheckTILCommand(userId, notionPageId, newCategory, title);
		List<UserCheckTILCommand> commands = Collections.singletonList(command);

		User user = User.builder()
			.id(userId)
			.githubAuthToken("valid-token")
			.githubOwner("owner1")
			.githubRepository("repo1")
			.githubDefaultBranch("main")
			.githubEmail("owner1@example.com")
			.build();

		NogiHistory history = NogiHistory.builder()
			.id(10L)
			.user(user)
			.notionPageId(notionPageId)
			.category(oldCategory)
			.title(title)
			.build();

		when(userRepository.findAllUserByIds(anyList()))
			.thenReturn(Collections.singletonList(user));
		when(userRepository.findAllNogiHistoryByNotionPageIds(anyList()))
			.thenReturn(Collections.singletonList(history));

		// when
		List<UserCheckTILResult> results = userService.checkTIL(commands);

		// then
		verify(userRepository, times(1)).findAllUserByIds(any());
		verify(userRepository, times(1)).findAllNogiHistoryByNotionPageIds(any());
		assertThat(results)
			.hasSize(1)
			.extracting(r -> tuple(
				r.userId(),
				r.type(),
				r.newCategory(),
				r.newTitle(),
				r.prevCategory(),
				r.prevTitle(),
				r.isSuccess()
			))
			.containsExactly(tuple(
				userId,
				NogiHistoryType.UPDATE_CATEGORY,
				newCategory,
				title,
				oldCategory,
				title,
				true
			));
	}
}