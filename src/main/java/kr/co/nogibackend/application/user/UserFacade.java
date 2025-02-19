package kr.co.nogibackend.application.user;

import static kr.co.nogibackend.response.code.UserResponseCode.*;

import org.springframework.stereotype.Service;

import kr.co.nogibackend.application.user.dto.UserFacadeCommand;
import kr.co.nogibackend.config.exception.GlobalException;
import kr.co.nogibackend.config.security.JwtProvider;
import kr.co.nogibackend.domain.github.GithubService;
import kr.co.nogibackend.domain.github.dto.command.GithubAddCollaboratorCommand;
import kr.co.nogibackend.domain.github.dto.command.GithubGetRepositoryCommand;
import kr.co.nogibackend.domain.github.dto.info.GithubRepoInfo;
import kr.co.nogibackend.domain.github.dto.request.GithubOAuthAccessTokenRequest;
import kr.co.nogibackend.domain.github.dto.result.GithubUserResult;
import kr.co.nogibackend.domain.user.UserService;
import kr.co.nogibackend.domain.user.dto.command.UserUpdateCommand;
import kr.co.nogibackend.domain.user.dto.info.UserInfo;
import kr.co.nogibackend.domain.user.dto.info.UserLoginByGithubInfo;
import kr.co.nogibackend.domain.user.dto.result.UserResult;
import lombok.RequiredArgsConstructor;

/*
  Package Name : kr.co.nogibackend.application.user
  File Name    : UserFacade
  Author       : won taek oh
  Created Date : 25. 2. 13.
  Description  :
 */
@Service
@RequiredArgsConstructor
public class UserFacade {

	private final GithubService githubService;
	private final UserService userService;
	private final JwtProvider jwtProvider;

	public UserLoginByGithubInfo loginByGithub(UserFacadeCommand.GithubLogin command) {
		// 1. access token 가져오기
		String githubAccessToken = githubService.getAccessToken(
			new GithubOAuthAccessTokenRequest(
				command.clientId(),
				command.clientSecret(),
				command.code(),
				"public_repo"
			)
		).accessToken();

		// 2. user 정보 가져오기
		GithubUserResult githubUserResult = githubService.getUserInfo(githubAccessToken);

		// 3. user 정보 저장하기
		UserResult savedUserResult =
			userService.createOrUpdateUser(UserUpdateCommand.from(githubUserResult, githubAccessToken));

		// 4. access toekn 발급하기(nogi token)
		String nogiAccessToken = jwtProvider.generateToken(savedUserResult.id(), savedUserResult.role());

		return UserLoginByGithubInfo.from(savedUserResult, nogiAccessToken);
	}

	public UserInfo updateUserAndCreateRepo(UserUpdateCommand command) {

		// 1. user 정보 가져오기
		UserResult userResult = userService.findUserByIdForFacade(command.getId());

		// 2. user 에 repository 가 없거나 변경된 경우 repository 생성
		if (
			userResult.githubRepository() == null ||
				!userResult.githubRepository().equals(command.getGithubRepository())
		) {
			GithubRepoInfo githubRepoInfo = githubService.createRepository(
				command.getGithubRepository(),
				userResult.githubAuthToken()
			);
			// default branch 수정
			command.setGithubDefaultBranch(githubRepoInfo.defaultBranch());

			// nogi-bot 을 collaborator 로 추가
			UserResult nogiBot = userService.findNogiBot()
				.orElseThrow(() -> new GlobalException(F_NOT_FOUND_USER));
			githubService.addCollaborator(
				new GithubAddCollaboratorCommand(
					userResult.githubOwner(),
					command.getGithubRepository(),
					nogiBot.githubOwner(),
					userResult.githubAuthToken()
				)
			);
		}

		// 3. DB 에 user 정보 업데이트
		return userService.updateUser(command);
	}

	public void validateRepositoryName(
		UserFacadeCommand.ValidateRepositoryName command
	) {
		UserResult userResult = userService.findUserByIdForFacade(command.userId());

		githubService.validateRepositoryName(
			new GithubGetRepositoryCommand(
				userResult.githubOwner(),
				command.repositoryName(),
				userResult.githubAuthToken()
			)
		);
	}
}
