package kr.co.nogibackend.application.user;

import static kr.co.nogibackend.response.code.GitResponseCode.*;
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

		if (
			// 2-1. user 에 repository 가 없을 경우 -> 생성
			userResult.githubRepository() == null
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
		} else if (
			// 2-2. user 에 repository 가 있으면서 repository 이름이 변경되었을 경우 -> 수정
			!userResult.githubRepository().equals(command.getGithubRepository())
		) {
			githubService.updateRepository(
				userResult.githubOwner(),
				userResult.githubRepository(),
				command.getGithubRepository(),
				userResult.githubAuthToken()
			);
		}

		// 3. DB 에 user 정보 업데이트
		return userService.updateUser(command);
	}

	public void validateRepositoryName(
		UserFacadeCommand.ValidateRepositoryName command
	) {
		UserResult userResult = userService.findUserByIdForFacade(command.userId());

		// 현재 사용중인 repository 이름이랑 같은 값인 경우
		if (command.repositoryName().equals(userResult.githubRepository())) {
			throw new GlobalException(F_ALREADY_USING_REPOSITORY_NAME);
		}

		githubService.validateRepositoryName(
			new GithubGetRepositoryCommand(
				userResult.githubOwner(),
				command.repositoryName(),
				userResult.githubAuthToken()
			)
		);
	}
}
