package kr.co.nogibackend.domain.github.dto.command;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.co.nogibackend.domain.user.dto.result.UserResult;

public record GithubNotifyCommand(
	Map<Long, GithubUser> userMap
) {
	public record GithubUser(
		Long id,
		String authToken,
		String Repo,
		String email,
		String owner
	) {
	}

	public static GithubNotifyCommand from(
		List<UserResult> userResultList
	) {
		Map<Long, GithubUser> githubUserMap = userResultList.stream().map(userResult -> new GithubUser(
			userResult.id(),
			userResult.githubAuthToken(),
			userResult.githubRepository(),
			userResult.githubEmail(),
			userResult.githubOwner()
		)).collect(Collectors.toMap(GithubUser::id, v -> v));
		return new GithubNotifyCommand(githubUserMap);
	}
}
