package kr.co.nogibackend.interfaces.user.response;


import java.util.List;
import kr.co.nogibackend.domain.user.entity.User;

public record UserAccountResponse(
		Long id,
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

	public static List<UserAccountResponse> from(List<User> users) {
		return
				users
						.stream()
						.map(user ->
								new UserAccountResponse(
										user.getId()
										, user.getGithubId()
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
