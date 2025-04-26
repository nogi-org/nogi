package kr.co.nogibackend.domain.user.entity;

import static kr.co.nogibackend.global.response.code.UserResponseCode.F_REQUIRE_NOTION_INFO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.nogibackend.domain.base.BaseEntity;
import kr.co.nogibackend.domain.user.command.UserUpdateCommand;
import kr.co.nogibackend.global.config.exception.GlobalException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.StringUtils;

@Table(
		name = "tb_user"
)
@Getter
@Entity
@Audited
@Builder
@EnableJpaAuditing
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE tb_user SET deleted = true, deleted_on = CURRENT_TIMESTAMP WHERE id = ?")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private Role role;
	private String notionAccessToken;
	private String notionPageId; // notionDatabase 가 있는 page 의 id
	@Column(nullable = true, length = 255, unique = true)
	private String notionDatabaseId;
	private Long githubId;// github user 의 고유 값
	private String githubAuthToken;
	private String githubRepository;
	private String githubDefaultBranch;
	private String githubEmail;
	private String githubOwner;
	@Builder.Default
	private Boolean isNotificationAllowed = true;
	private String selfIntroduction;


	public User update(UserUpdateCommand command) {
		if (StringUtils.hasText(command.getNotionAccessToken())) {
			this.notionAccessToken = command.getNotionAccessToken();
		}
		if (StringUtils.hasText(command.getNotionPageId())) {
			this.notionPageId = command.getNotionPageId();
		}
		if (StringUtils.hasText(command.getNotionDatabaseId())) {
			this.notionDatabaseId = command.getNotionDatabaseId();
		}
		if (StringUtils.hasText(command.getGithubAuthToken())) {
			this.githubAuthToken = command.getGithubAuthToken();
		}
		if (StringUtils.hasText(command.getGithubRepository())) {
			this.githubRepository = command.getGithubRepository();
		}
		if (StringUtils.hasText(command.getGithubDefaultBranch())) {
			this.githubDefaultBranch = command.getGithubDefaultBranch();
		}
		if (StringUtils.hasText(command.getGithubEmail())) {
			this.githubEmail = command.getGithubEmail();
		}
		if (StringUtils.hasText(command.getGithubOwner())) {
			this.githubOwner = command.getGithubOwner();
		}
		if (command.getIsNotificationAllowed() != null) {
			this.isNotificationAllowed = command.getIsNotificationAllowed();
		}
		if (StringUtils.hasText(command.getSelfIntroduction())) {
			this.selfIntroduction = command.getSelfIntroduction();
		}
		return this;
	}

	public boolean isEmptyNotionPageId() {
		return this.notionPageId == null;
	}

	public boolean isEmptyNotionDatabaseId() {
		return this.notionDatabaseId == null;
	}

	public boolean isEmptyNotionAccessToken() {
		return this.notionAccessToken == null;
	}

	public boolean hasNotionAccessToken() {
		return this.notionAccessToken != null;
	}

	public boolean hasNotionDatabaseId() {
		return this.notionDatabaseId != null;
	}

	public void updateNotionPageId(String pageId) {
		this.notionPageId = pageId;
	}

	public void validateHasNotionTokenOrDatabaseId() {
		if (!this.hasNotionAccessToken() || !this.hasNotionDatabaseId()) {
			throw new GlobalException(F_REQUIRE_NOTION_INFO);
		}
	}

	public enum Role {
		ADMIN, USER
	}
}
