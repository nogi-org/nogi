package kr.co.nogibackend.domain.user;

import java.util.Optional;

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

	public void update(UserUpdateCommand command) {
		Optional.ofNullable(command.getNotionAuthToken()).ifPresent(value -> this.notionAuthToken = value);
		Optional.ofNullable(command.getNotionDatabaseId()).ifPresent(value -> this.notionDatabaseId = value);
		Optional.ofNullable(command.getGithubAuthToken()).ifPresent(value -> this.githubAuthToken = value);
		Optional.ofNullable(command.getGithubRepository()).ifPresent(value -> this.githubRepository = value);
		Optional.ofNullable(command.getGithubDefaultBranch()).ifPresent(value -> this.githubDefaultBranch = value);
		Optional.ofNullable(command.getGithubEmail()).ifPresent(value -> this.githubEmail = value);
		Optional.ofNullable(command.getGithubOwner()).ifPresent(value -> this.githubOwner = value);
	}

	public enum Role {
		NOGI_BOT, ADMIN, USER
	}
}
