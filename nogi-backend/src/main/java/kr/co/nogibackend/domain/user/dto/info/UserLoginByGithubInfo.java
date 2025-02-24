package kr.co.nogibackend.domain.user.dto.info;

import kr.co.nogibackend.domain.user.dto.result.UserResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginByGithubInfo {

	private UserInfo userInfo;
	private String accessToken;

	public static UserLoginByGithubInfo from(UserResult userresult, String accessToken) {
		return
			UserLoginByGithubInfo
				.builder()
				.userInfo(
					new UserInfo(
						userresult.id(),
						userresult.role(),
						userresult.notionBotToken(),
						userresult.notionDatabaseId(),
						userresult.githubAuthToken(),
						userresult.githubRepository(),
						userresult.githubDefaultBranch(),
						userresult.githubEmail(),
						userresult.githubOwner()
					)
				)
				.accessToken(accessToken)
				.build();
	}

	/*
	사용자가 입력해야할 정보가 있는지 확인하기 위한 메서드
	 */
	public boolean isRequireUserInfo() {
		return userInfo.notionBotToken() == null ||
			userInfo.notionDatabaseId() == null ||
			userInfo.githubRepository() == null ||
			userInfo.githubDefaultBranch() == null;
	}
}
