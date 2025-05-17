package kr.co.nogibackend.application.user;

import static kr.co.nogibackend.global.response.code.UserResponseCode.F_NOT_FOUND_USER;

import kr.co.nogibackend.domain.github.command.GithubAddCollaboratorCommand;
import kr.co.nogibackend.domain.github.command.GithubGetRepositoryCommand;
import kr.co.nogibackend.domain.github.result.GithubRepoResult;
import kr.co.nogibackend.domain.github.service.GithubService;
import kr.co.nogibackend.domain.user.command.UserUpdateCommand;
import kr.co.nogibackend.domain.user.result.UserReslut;
import kr.co.nogibackend.domain.user.result.UserResult;
import kr.co.nogibackend.domain.user.service.UserService;
import kr.co.nogibackend.global.config.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserUpdateFacade {

	private final UserService userService;
	private final GithubService githubService;

	public UserReslut updateUserAndCreateRepo(UserUpdateCommand command) {
		// 1. user 정보 가져오기
		UserResult userResult = userService.findUserByIdForFacade(command.getId());

		// 2. github 에 실제로 repo 가 존재하는지 확인하고 없으면 생성
		handleRepositoryCreationOrUpdate(command, userResult);

		// 3. DB 에 user 정보 업데이트
		return userService.updateUser(command);
	}

	private void handleRepositoryCreationOrUpdate(UserUpdateCommand command, UserResult userResult) {
		String newRepo = command.getGithubRepository();
		if (!StringUtils.hasText(newRepo)) {
			return;
		}

		boolean isDeletedOnGithub = isRepositoryDeletedOnGithub(
				userResult.githubOwner(),
				newRepo,
				userResult.githubAuthToken()
		);

		// GitHub 에서 삭제된 상태일 때 새롭게 생성
		if (isDeletedOnGithub) {
			String defaultBranch = createRepositoryAndAddCollaborator(command, userResult);
			// update 하기 위해 default branch 를 저장
			command.setGithubDefaultBranch(defaultBranch);
		}
	}

	private boolean isRepositoryDeletedOnGithub(
			String githubOwner,
			String githubRepository,
			String githubAuthToken
	) {
		return githubService.isUniqueRepositoryName(
				new GithubGetRepositoryCommand(
						githubOwner,
						githubRepository,
						githubAuthToken
				)
		);
	}

	private String createRepositoryAndAddCollaborator(UserUpdateCommand command,
			UserResult userResult) {
		GithubRepoResult githubRepoResult = githubService.createRepository(
				command.getGithubRepository(), userResult.githubAuthToken()
		);

		UserResult nogiBot = userService.findNogiBot()
				.orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));

		githubService.addCollaborator(
				new GithubAddCollaboratorCommand(
						userResult.githubOwner(), command.getGithubRepository(),
						nogiBot.githubOwner(), userResult.githubAuthToken()
				)
		);
		return githubRepoResult.defaultBranch();
	}

}
