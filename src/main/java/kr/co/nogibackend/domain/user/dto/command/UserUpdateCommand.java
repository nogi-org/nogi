package kr.co.nogibackend.domain.user.dto.command;

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
public class UserUpdateCommand {

	private String id;
	private String notionAuthToken;
	private String notionDatabaseId;
	private String githubAuthToken;
	private String githubRepository;
	private String githubDefaultBranch;
	private String githubEmail;
	private String githubOwner;
}
