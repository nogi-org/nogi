package kr.co.nogibackend.domain.admin.dto.response;


import java.util.List;
import kr.co.nogibackend.domain.user.User;

public record UserInfoResponse(
		Long githubId,
		String role,
		String owner,
		String email,
		String repository,
		String branch,
		String pageId,
		boolean hasGithubToken,
		boolean hasNotionToken,
		boolean hasNotionDatabaseId
) {

	public static List<UserInfoResponse> from(List<User> users) {
		return
				users
						.stream()
						.map(user ->
								new UserInfoResponse(
										user.getGithubId()
										, user.getRole().name()
										, user.getGithubOwner()
										, user.getGithubEmail()
										, user.getGithubRepository()
										, user.getGithubDefaultBranch()
										, user.getNotionPageId()
										, user.getGithubAuthToken() != null
										, user.getNotionAccessToken() != null
										, user.getNotionDatabaseId() != null
								)
						)
						.toList();
	}

}
