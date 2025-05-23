package kr.co.nogibackend.application.user;

import kr.co.nogibackend.application.notion.command.NotionLoginEventCommand;
import kr.co.nogibackend.application.user.command.UserFacadeCommand;
import kr.co.nogibackend.domain.github.command.GithubOAuthAccessTokenCommand;
import kr.co.nogibackend.domain.github.result.GithubUserResult;
import kr.co.nogibackend.domain.github.service.GithubService;
import kr.co.nogibackend.domain.notion.result.NotionTokenPageIdResult;
import kr.co.nogibackend.domain.notion.service.NotionAccountService;
import kr.co.nogibackend.domain.user.command.UserUpdateCommand;
import kr.co.nogibackend.domain.user.result.UserLoginByGithubResult;
import kr.co.nogibackend.domain.user.result.UserLoginByNotionResult;
import kr.co.nogibackend.domain.user.result.UserSinUpOrUpdateResult;
import kr.co.nogibackend.domain.user.service.UserService;
import kr.co.nogibackend.global.config.audit.AuditContext;
import kr.co.nogibackend.global.config.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserLoginFacade {

	private final UserService userService;
	private final GithubService githubService;
	private final NotionAccountService notionAccountService;
	private final JwtProvider jwtProvider;
	private final ApplicationEventPublisher applicationEventPublisher;

	public UserLoginByGithubResult loginByGithub(UserFacadeCommand.GithubLogin command) {
		try {
			// 1. access token 가져오기
			String githubAccessToken = githubService.getAccessToken(
					new GithubOAuthAccessTokenCommand(
							command.clientId(),
							command.clientSecret(),
							command.code(),
							"public_repo"
					)
			).accessToken();

			// 2. user 정보 가져오기
			GithubUserResult githubUserResult = githubService.getUserInfo(githubAccessToken);

			// 3. AuditContext 에 저장(DB auditing 을 위함)
			AuditContext.setUserId(0L);

			// 4. user 정보 저장하기
			UserSinUpOrUpdateResult sinUpOrUpdateResult =
					userService.signUpOrUpdateUser(
							githubUserResult.id(),
							UserUpdateCommand.fromGithubLogin(githubUserResult, githubAccessToken)
					);

			// 5. access toekn 발급하기(nogi token)
			String nogiAccessToken = jwtProvider.generateToken(
					sinUpOrUpdateResult.id(),
					sinUpOrUpdateResult.role()
			);

			// 6. 신규유저일 경우 알림보내개(경축!)
			if (sinUpOrUpdateResult.isSinUp()) {
				userService.sendSinUpNotification(
						sinUpOrUpdateResult.id(),
						sinUpOrUpdateResult.githubOwner()
				);
			}

			return UserLoginByGithubResult.from(
					sinUpOrUpdateResult,
					nogiAccessToken,
					true,
					"Github 로그인을 성공하였어요!"
			);
		} catch (Exception e) {
			return UserLoginByGithubResult.from(false, "Github 로그인에 실패했어요.");
		}
	}

	public UserLoginByNotionResult loginByNotion(UserFacadeCommand.NotionLogin command) {
		try {
			// 1. access token 가져오기
			NotionTokenPageIdResult notionResult = notionAccountService.getAccessToken(
					command.getBasicToken(),
					command.code(),
					command.notionRedirectUri()
			);
			String notionPageId = notionResult.notionPageId();
			// 1-1. notion page id 가 없는 경우 -> 유저가 개발자가 제공한 템플릿을 선택하지 않은 경우
			if (!StringUtils.hasText(notionPageId)) {
				String message = "개발자가 제공한 템플릿을 선택해야해주세요!";
				return new UserLoginByNotionResult(false, message);
			}

			// 2. database 정보 가져와서 user 정보 업데이트(notionDatabaseId 저장)
			applicationEventPublisher.publishEvent(
					new NotionLoginEventCommand(
							command.userId(),
							notionResult.notionAccessToken(),
							notionResult.notionPageId()
					)
			);

			// 3. AuditContext 에 저장(DB auditing 을 위함) -> callbackUrl 로 동작하는 메서드라 따로 처리
			AuditContext.setUserId(command.userId());

			// 4. user 정보 업데이트(notionPageId 저장)
			userService.updateUser(
					UserUpdateCommand.builder()
							.id(command.userId())
							.notionAccessToken(notionResult.notionAccessToken())
							.notionPageId(notionResult.notionPageId())
							.build()
			);

			return new UserLoginByNotionResult(true, "Notion 과 성공적으로 연결하였어요!");
		} catch (Exception e) {
			return new UserLoginByNotionResult(false, "Notion 과 연결하는데 실패했어요.");
		}
	}

}
