package kr.co.nogibackend.domain.user.result;

import kr.co.nogibackend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginByGithubResult {

	private Long userId;
	private User.Role role;
	private String notionPageId;
	private String githubRepository;
	private String accessToken;
	private boolean isSuccess;
	private String message;

	public static UserLoginByGithubResult from(
			UserSinUpOrUpdateResult userResult,
			String accessToken,
			boolean isSuccess,
			String message
	) {
		return
				UserLoginByGithubResult
						.builder()
						.userId(userResult.id())
						.role(userResult.role())
						.notionPageId(userResult.notionPageId())
						.githubRepository(userResult.githubRepository())
						.accessToken(accessToken)
						.isSuccess(isSuccess)
						.message(message)
						.build();
	}

	public static UserLoginByGithubResult from(
			boolean isSuccess,
			String message
	) {
		return
				UserLoginByGithubResult
						.builder()
						.isSuccess(isSuccess)
						.message(message)
						.build();
	}

	/*
	사용자의 Notion 정보가 있는지 확인하기 위한 메서드
	 */
	public boolean isRequireNotionInfo() {
		return !StringUtils.hasText(notionPageId);
	}

	/*
	사용자의 Github 정보가 있는지 확인하기 위한 메서드
	 */
	public boolean isRequireGithubInfo() {
		return !StringUtils.hasText(githubRepository);
	}
}
