package kr.co.nogibackend.domain.user;

import org.springframework.util.StringUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.nogibackend.domain.user.dto.command.UserUpdateCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
  Package Name : kr.co.nogibackend.domain
  File Name    : User
  Author       : superpil
  Created Date : 25. 2. 1.
  Description  :
 */
@Table(
	name = "tb_user"
)
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private Role role;
	private String notionAuthToken;
	private String notionDatabaseId;
	private String githubAuthToken;
	private String githubRepository;
	private String githubDefaultBranch;
	private String githubEmail;
	private String githubOwner;

	public User update(UserUpdateCommand command) {
		if (StringUtils.hasText(command.getNotionAuthToken()))
			this.notionAuthToken = command.getNotionAuthToken();
		if (StringUtils.hasText(command.getNotionDatabaseId()))
			this.notionDatabaseId = command.getNotionDatabaseId();
		if (StringUtils.hasText(command.getGithubAuthToken()))
			this.githubAuthToken = command.getGithubAuthToken();
		if (StringUtils.hasText(command.getGithubRepository()))
			this.githubRepository = command.getGithubRepository();
		if (StringUtils.hasText(command.getGithubDefaultBranch()))
			this.githubDefaultBranch = command.getGithubDefaultBranch();
		if (StringUtils.hasText(command.getGithubEmail()))
			this.githubEmail = command.getGithubEmail();
		if (StringUtils.hasText(command.getGithubOwner()))
			this.githubOwner = command.getGithubOwner();
		return this;
	}

	public enum Role {
		NOGI_BOT, ADMIN, USER
	}
}
